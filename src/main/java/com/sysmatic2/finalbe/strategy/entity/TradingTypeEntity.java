package com.sysmatic2.finalbe.strategy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "trading_type")
@Getter
@Setter
@ToString
public class TradingTypeEntity extends Auditable{

    @Id
    @Column(name="trading_type_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer tradingTypeId; // 매매유형 ID

    @Column(name="trading_type_order", nullable = false,unique = true)
    private Integer tradingTypeOrder; // 매매유형순서

    @Column(name="trading_type_name",nullable = false, unique = true)
    private String tradingTypeName; // 매매유형명

    @Column(name="trading_type_icon", nullable = false)
    private String tradingTypeIcon; // 매매유형아이콘

    @Column(name="trading_type_description")
    private String tradingTypeDescription; // 매매유형 설명

    @Column(name="is_active", nullable = false)
    private String isActive; // 사용유무
}