package com.codingclub.member.controller;

import com.codingclub.member.dto.MemberResponse;
import com.codingclub.member.model.Member;
import com.codingclub.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setId(1L);
        testMember.setUserId(100L);
        testMember.setFullName("John Doe");
    }

    @Test
    void testGetMemberByUserId() {
        when(memberService.getMemberResponseByUserId(100L)).thenReturn(MemberResponse.from(testMember, List.of(), null));

        ResponseEntity<MemberResponse> response = memberController.getMemberByUserId(100L);

        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getFullName());
    }

    @Test
    void testUpdateMyProfile() {
        Member updatedData = new Member();
        updatedData.setFullName("John Smith");

        Member savedMember = new Member();
        savedMember.setId(1L);
        savedMember.setUserId(100L);
        savedMember.setFullName("John Smith");

        when(memberService.updateMember(eq(100L), any(Member.class))).thenReturn(
                MemberResponse.from(savedMember, List.of(), null));

        ResponseEntity<MemberResponse> response = memberController.updateMyProfile(
                "100", "MEMBER", "", "0", "false", "true", "", updatedData);

        assertNotNull(response.getBody());
        assertEquals("John Smith", response.getBody().getFullName());
    }
}
