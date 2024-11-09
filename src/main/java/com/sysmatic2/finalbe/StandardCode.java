package com.sysmatic2.finalbe;

import com.sysmatic2.finalbe.strategy.entity.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "standard_code")
@Getter
@Setter
@ToString
public class StandardCode extends Auditable {
    @Id
    private String code; // 코드

    @Column(name = "code_type_name", nullable = false)
    private String codeTypeName; // 코드유형명

    @Column(name = "code_name", nullable = false)
    private String codeName; // 코드명

    @Column(name = "code_description")
    private String codeDescription; // 코드 설명

    @Column(name="sort_order", nullable = false)
    private Short sortOrder; // 정렬 순서

    @Column(name="is_use", nullable = false)
    private String isUse; // 사용여부
}
