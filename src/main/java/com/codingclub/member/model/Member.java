package com.codingclub.member.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "members")
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true)
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "role_title")
    private String roleTitle;

    @Column(name = "dev_stack")
    private String devStack;

    @Column(name = "linkedin_url")
    private String linkedinUrl;

    @Column(name = "portfolio_url")
    private String portfolioUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
