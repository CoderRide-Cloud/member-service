package com.codingclub.member.service;

import com.codingclub.common.exception.ResourceNotFoundException;
import com.codingclub.member.dto.MemberResponse;
import com.codingclub.member.model.Member;
import com.codingclub.member.model.MemberSkill;
import com.codingclub.member.repository.MemberRepository;
import com.codingclub.member.repository.MemberSkillRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.codingclub.common.dto.UserDto;
import com.codingclub.member.client.AuthServiceClient;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final MemberSkillRepository memberSkillRepository;

    private final AuthServiceClient authServiceClient;

    public MemberService(MemberRepository memberRepository, MemberSkillRepository memberSkillRepository, AuthServiceClient authServiceClient) {
        this.memberRepository = memberRepository;
        this.memberSkillRepository = memberSkillRepository;
        this.authServiceClient = authServiceClient;
    }

    public List<MemberResponse> getAllMembers(int skip, int limit, List<Long> skillIds) {
        List<Member> members = memberRepository.findAll(PageRequest.of(skip / Math.max(limit, 1), Math.max(limit, 1))).getContent();

        List<Member> filteredMembers = members.stream()
                .filter(member -> skillIds == null || skillIds.isEmpty() || hasAllSkills(member.getId(), skillIds))
                .toList();

        List<Long> userIds = filteredMembers.stream().map(Member::getUserId).collect(Collectors.toList());
        List<UserDto> users = new ArrayList<>();
        try {
            if (!userIds.isEmpty()) {
                users = authServiceClient.getUsersBulk(userIds);
            }
        } catch (Exception e) {
            // Log error, proceed with null users
            System.err.println("Failed to fetch users bulk: " + e.getMessage());
        }

        Map<Long, UserDto> userMap = users.stream().collect(Collectors.toMap(UserDto::getId, u -> u, (u1, u2) -> u1));

        return filteredMembers.stream()
                .map(member -> {
                    List<Long> memberSkillIds = memberSkillRepository.findByMemberId(member.getId()).stream()
                            .map(MemberSkill::getSkillId)
                            .collect(Collectors.toList());
                    return MemberResponse.from(member, memberSkillIds, userMap.get(member.getUserId()));
                })
                .collect(Collectors.toList());
    }

    public MemberResponse getMemberResponseById(Long id) {
        return toResponse(getMemberById(id));
    }

    public MemberResponse getMemberResponseByUserId(Long userId) {
        return toResponse(getMemberByUserId(userId));
    }

    public Member getMemberByUserId(Long userId) {
        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found for user ID: " + userId));
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));
    }

    public MemberResponse updateMember(Long userId, Member updatedData) {
        Member existingMember = getMemberByUserId(userId);

        if (updatedData.getFullName() != null) existingMember.setFullName(updatedData.getFullName());
        if (updatedData.getBio() != null) existingMember.setBio(updatedData.getBio());
        if (updatedData.getRoleTitle() != null) existingMember.setRoleTitle(updatedData.getRoleTitle());
        if (updatedData.getDevStack() != null) existingMember.setDevStack(updatedData.getDevStack());
        if (updatedData.getLinkedinUrl() != null) existingMember.setLinkedinUrl(updatedData.getLinkedinUrl());
        if (updatedData.getPortfolioUrl() != null) existingMember.setPortfolioUrl(updatedData.getPortfolioUrl());

        return toResponse(memberRepository.save(existingMember));
    }

    @Transactional
    public MemberResponse addSkills(Long userId, List<Long> skillIds) {
        Member member = getMemberByUserId(userId);
        List<MemberSkill> existing = memberSkillRepository.findByMemberId(member.getId());
        List<Long> existingIds = existing.stream().map(MemberSkill::getSkillId).toList();

        for (Long skillId : skillIds) {
            if (!existingIds.contains(skillId)) {
                MemberSkill memberSkill = new MemberSkill();
                memberSkill.setMemberId(member.getId());
                memberSkill.setSkillId(skillId);
                memberSkillRepository.save(memberSkill);
            }
        }

        return toResponse(member);
    }

    @Transactional
    public MemberResponse removeSkill(Long userId, Long skillId) {
        Member member = getMemberByUserId(userId);
        memberSkillRepository.deleteByMemberIdAndSkillId(member.getId(), skillId);
        return toResponse(member);
    }

    private boolean hasAllSkills(Long memberId, List<Long> skillIds) {
        for (Long skillId : skillIds) {
            if (!memberSkillRepository.existsByMemberIdAndSkillId(memberId, skillId)) {
                return false;
            }
        }
        return true;
    }

    private MemberResponse toResponse(Member member) {
        List<Long> skillIds = memberSkillRepository.findByMemberId(member.getId()).stream()
                .map(MemberSkill::getSkillId)
                .collect(Collectors.toCollection(ArrayList::new));

        UserDto userDto = null;
        try {
            List<UserDto> users = authServiceClient.getUsersBulk(List.of(member.getUserId()));
            if (users != null && !users.isEmpty()) {
                userDto = users.getFirst();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch user " + member.getUserId() + ": " + e.getMessage());
        }

        return MemberResponse.from(member, skillIds, userDto);
    }
}
