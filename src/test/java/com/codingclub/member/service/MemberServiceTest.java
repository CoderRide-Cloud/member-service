package com.codingclub.member.service;

import com.codingclub.common.exception.ResourceNotFoundException;
import com.codingclub.member.dto.MemberResponse;
import com.codingclub.member.model.Member;
import com.codingclub.member.repository.MemberRepository;
import com.codingclub.member.repository.MemberSkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    // Added the missing repository mock required by toResponse()
    @Mock
    private MemberSkillRepository memberSkillRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setId(1L);
        testMember.setUserId(100L);
        testMember.setFullName("John Doe");
        testMember.setBio("Software Engineer");
    }

    @Test
    void testGetMemberByUserId_Found() {
        when(memberRepository.findByUserId(100L)).thenReturn(Optional.of(testMember));

        Member result = memberService.getMemberByUserId(100L);

        assertNotNull(result);
        assertEquals("John Doe", result.getFullName());
    }

    @Test
    void testGetMemberByUserId_NotFound() {
        when(memberRepository.findByUserId(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getMemberByUserId(999L));
    }

    @Test
    void testUpdateMember() {
        when(memberRepository.findByUserId(100L)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock the skill repository call triggered inside toResponse()
        when(memberSkillRepository.findByMemberId(anyLong())).thenReturn(new ArrayList<>());

        Member updatedData = new Member();
        updatedData.setBio("Updated Bio");
        updatedData.setDevStack("Java, Spring Boot");

        // FIX: Changed type from Member to MemberResponse
        MemberResponse result = memberService.updateMember(100L, updatedData);

        assertEquals("Updated Bio", result.getBio());
        assertEquals("Java, Spring Boot", result.getDevStack());
        assertEquals("John Doe", result.getFullName()); // Unchanged fields should remain the same
    }
}