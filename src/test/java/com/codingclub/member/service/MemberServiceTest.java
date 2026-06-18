package com.codingclub.member.service;

import com.codingclub.common.exception.ResourceNotFoundException;
import com.codingclub.member.model.Member;
import com.codingclub.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

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

        Member updatedData = new Member();
        updatedData.setBio("Updated Bio");
        updatedData.setDevStack("Java, Spring Boot");

        Member result = memberService.updateMember(100L, updatedData);

        assertEquals("Updated Bio", result.getBio());
        assertEquals("Java, Spring Boot", result.getDevStack());
        assertEquals("John Doe", result.getFullName()); // Unchanged fields should remain the same
    }
}
