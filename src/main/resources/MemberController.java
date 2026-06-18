package com.codingclub.member.controller;

import com.codingclub.member.model.Member;
import com.codingclub.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Member> getMemberByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(memberService.getMemberByUserId(userId));
    }

    @PutMapping("/me")
    public ResponseEntity<Member> updateMyProfile(
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestBody Member updatedData) {
        
        Long userId = Long.valueOf(userIdHeader);
        return ResponseEntity.ok(memberService.updateMember(userId, updatedData));
    }
}
