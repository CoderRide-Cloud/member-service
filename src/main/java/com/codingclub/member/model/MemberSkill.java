package com.codingclub.member.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "member_skills")
@IdClass(MemberSkillId.class)
public class MemberSkill {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "skill_id")
    private Long skillId;

    public MemberSkill() {
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof MemberSkill other)) return false;
        if (!other.canEqual(this)) return false;
        final Object this$memberId = this.getMemberId();
        final Object other$memberId = other.getMemberId();
        if (!Objects.equals(this$memberId, other$memberId)) return false;
        final Object this$skillId = this.getSkillId();
        final Object other$skillId = other.getSkillId();
        return Objects.equals(this$skillId, other$skillId);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof MemberSkill;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $memberId = this.getMemberId();
        result = result * PRIME + ($memberId == null ? 43 : $memberId.hashCode());
        final Object $skillId = this.getSkillId();
        result = result * PRIME + ($skillId == null ? 43 : $skillId.hashCode());
        return result;
    }

    public String toString() {
        return "MemberSkill(memberId=" + this.getMemberId() + ", skillId=" + this.getSkillId() + ")";
    }
}
