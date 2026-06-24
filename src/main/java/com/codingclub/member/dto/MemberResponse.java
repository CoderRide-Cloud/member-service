package com.codingclub.member.dto;

import com.codingclub.common.dto.UserDto;
import com.codingclub.member.model.Member;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MemberResponse {
    private Long id;
    private Long userId;
    private String fullName;
    private String bio;
    private String roleTitle;
    private String devStack;
    private String linkedinUrl;
    private String portfolioUrl;
    private List<Long> skillIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto user;

    public static MemberResponse from(Member member, List<Long> skillIds, UserDto user) {
        MemberResponse response = new MemberResponse();
        response.setId(member.getId());
        response.setUserId(member.getUserId());
        response.setFullName(member.getFullName());
        response.setBio(member.getBio());
        response.setRoleTitle(member.getRoleTitle());
        response.setDevStack(member.getDevStack());
        response.setLinkedinUrl(member.getLinkedinUrl());
        response.setPortfolioUrl(member.getPortfolioUrl());
        response.setSkillIds(skillIds);
        response.setCreatedAt(member.getCreatedAt());
        response.setUpdatedAt(member.getUpdatedAt());
        response.setUser(user);
        return response;
    }
}
