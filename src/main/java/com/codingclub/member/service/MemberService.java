package com.codingclub.member.service;

import com.codingclub.common.exception.ResourceNotFoundException;
import com.codingclub.member.model.Member;
import com.codingclub.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberByUserId(Long userId) {
        return memberRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found for user ID: " + userId));
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));
    }

    public Member updateMember(Long userId, Member updatedData) {
        Member existingMember = getMemberByUserId(userId);
        
        if (updatedData.getFullName() != null) existingMember.setFullName(updatedData.getFullName());
        if (updatedData.getBio() != null) existingMember.setBio(updatedData.getBio());
        if (updatedData.getRoleTitle() != null) existingMember.setRoleTitle(updatedData.getRoleTitle());
        if (updatedData.getDevStack() != null) existingMember.setDevStack(updatedData.getDevStack());
        if (updatedData.getLinkedinUrl() != null) existingMember.setLinkedinUrl(updatedData.getLinkedinUrl());
        if (updatedData.getPortfolioUrl() != null) existingMember.setPortfolioUrl(updatedData.getPortfolioUrl());
        
        return memberRepository.save(existingMember);
    }
}
