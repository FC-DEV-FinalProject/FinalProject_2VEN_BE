package com.sysmatic2.finalbe.strategy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    @CreatedBy
    @Column(name="created_by", updatable = false)
    private Long createdBy; // 최초작성자 ID

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 최초등록일시

    @LastModifiedBy
    @Column(name = "modified_by")
    private Long modifiedBy; // 최종수정자 ID

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt; // 최종수정일시
}