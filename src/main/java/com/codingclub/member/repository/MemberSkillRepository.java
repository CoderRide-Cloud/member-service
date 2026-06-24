package com.codingclub.member.repository;

import com.codingclub.member.model.MemberSkill;
import com.codingclub.member.model.MemberSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberSkillRepository extends JpaRepository<MemberSkill, MemberSkillId> {
    List<MemberSkill> findByMemberId(Long memberId);
    void deleteByMemberIdAndSkillId(Long memberId, Long skillId);
    boolean existsByMemberIdAndSkillId(Long memberId, Long skillId);
}
