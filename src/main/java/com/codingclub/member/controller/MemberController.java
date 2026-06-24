package com.codingclub.member.controller;

import com.codingclub.common.security.AuthUserContext;
import com.codingclub.common.security.AuthorizationService;
import com.codingclub.common.web.AuthContextResolver;
import com.codingclub.member.dto.MemberResponse;
import com.codingclub.member.model.Member;
import com.codingclub.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthContextResolver authContextResolver;

    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers(
            @RequestParam(defaultValue = "0") int skip,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(required = false) String skillIds) {

        List<Long> parsedSkillIds = skillIds == null || skillIds.isBlank()
                ? null
                : Arrays.stream(skillIds.split(",")).map(Long::valueOf).collect(Collectors.toList());

        return ResponseEntity.ok(memberService.getAllMembers(skip, limit, parsedSkillIds));
    }

    @GetMapping("/profile")
    public ResponseEntity<MemberResponse> getAuthenticatedProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Permissions", required = false) String permissions,
            @RequestHeader(value = "X-User-Position", required = false) String position,
            @RequestHeader(value = "X-User-Is-Lead", required = false) String isLead,
            @RequestHeader(value = "X-User-Is-Active", required = false) String isActive,
            @RequestHeader(value = "X-User-Custom-Role-Id", required = false) String customRoleId) {

        AuthUserContext authUser = authContextResolver.resolve(userId, role, permissions, position, isLead, isActive, customRoleId);
        authorizationService.requireActive(authUser);
        return ResponseEntity.ok(memberService.getMemberResponseByUserId(authUser.getUserId()));
    }

    @PutMapping("/profile")
    public ResponseEntity<MemberResponse> updateProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Permissions", required = false) String permissions,
            @RequestHeader(value = "X-User-Position", required = false) String position,
            @RequestHeader(value = "X-User-Is-Lead", required = false) String isLead,
            @RequestHeader(value = "X-User-Is-Active", required = false) String isActive,
            @RequestHeader(value = "X-User-Custom-Role-Id", required = false) String customRoleId,
            @RequestBody Member updatedData) {

        AuthUserContext authUser = authContextResolver.resolve(userId, role, permissions, position, isLead, isActive, customRoleId);
        authorizationService.requireActive(authUser);
        return ResponseEntity.ok(memberService.updateMember(authUser.getUserId(), updatedData));
    }

    @PostMapping("/skills")
    public ResponseEntity<MemberResponse> addSkills(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Permissions", required = false) String permissions,
            @RequestHeader(value = "X-User-Position", required = false) String position,
            @RequestHeader(value = "X-User-Is-Lead", required = false) String isLead,
            @RequestHeader(value = "X-User-Is-Active", required = false) String isActive,
            @RequestHeader(value = "X-User-Custom-Role-Id", required = false) String customRoleId,
            @RequestBody Map<String, Object> payload) {

        AuthUserContext authUser = authContextResolver.resolve(userId, role, permissions, position, isLead, isActive, customRoleId);
        authorizationService.requireActive(authUser);

        List<Long> skillIds = parseSkillIds(payload);
        if (skillIds.isEmpty()) {
            throw new IllegalArgumentException("skillIds or skillId is required");
        }

        return ResponseEntity.ok(memberService.addSkills(authUser.getUserId(), skillIds));
    }

    private List<Long> parseSkillIds(Map<String, Object> payload) {
        Object skillIdsObj = payload.get("skillIds");
        if (skillIdsObj instanceof List<?> list) {
            return list.stream()
                    .map(id -> ((Number) id).longValue())
                    .collect(Collectors.toList());
        }
        Object skillIdObj = payload.get("skillId");
        if (skillIdObj instanceof Number number) {
            return List.of(number.longValue());
        }
        return List.of();
    }

    @DeleteMapping("/skills/{skillId}")
    public ResponseEntity<MemberResponse> removeSkill(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Permissions", required = false) String permissions,
            @RequestHeader(value = "X-User-Position", required = false) String position,
            @RequestHeader(value = "X-User-Is-Lead", required = false) String isLead,
            @RequestHeader(value = "X-User-Is-Active", required = false) String isActive,
            @RequestHeader(value = "X-User-Custom-Role-Id", required = false) String customRoleId,
            @PathVariable Long skillId) {

        AuthUserContext authUser = authContextResolver.resolve(userId, role, permissions, position, isLead, isActive, customRoleId);
        authorizationService.requireActive(authUser);
        return ResponseEntity.ok(memberService.removeSkill(authUser.getUserId(), skillId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberResponseById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<MemberResponse> getMemberByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(memberService.getMemberResponseByUserId(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<MemberResponse> updateMyProfile(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Permissions", required = false) String permissions,
            @RequestHeader(value = "X-User-Position", required = false) String position,
            @RequestHeader(value = "X-User-Is-Lead", required = false) String isLead,
            @RequestHeader(value = "X-User-Is-Active", required = false) String isActive,
            @RequestHeader(value = "X-User-Custom-Role-Id", required = false) String customRoleId,
            @RequestBody Member updatedData) {

        AuthUserContext authUser = authContextResolver.resolve(userId, role, permissions, position, isLead, isActive, customRoleId);
        authorizationService.requireActive(authUser);
        return ResponseEntity.ok(memberService.updateMember(authUser.getUserId(), updatedData));
    }
}
