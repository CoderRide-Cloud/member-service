package com.codingclub.member.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class MemberSkillId implements Serializable {
    private Long memberId;
    private Long skillId;
}
