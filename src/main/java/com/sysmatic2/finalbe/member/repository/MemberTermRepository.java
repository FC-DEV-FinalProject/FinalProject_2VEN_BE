package com.sysmatic2.finalbe.member.repository;

import com.sysmatic2.finalbe.member.entity.MemberTermEntity;
import com.sysmatic2.finalbe.member.entity.TermTypeMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTermRepository extends JpaRepository<MemberTermEntity, TermTypeMemberId> {
}
