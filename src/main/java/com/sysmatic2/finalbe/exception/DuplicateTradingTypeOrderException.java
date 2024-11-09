package com.sysmatic2.finalbe.exception;

public class DuplicateTradingTypeOrderException extends RuntimeException {
    public DuplicateTradingTypeOrderException(Integer order) {
        super("이미 존재하는 매매유형 순서입니다. Order: " + order);
    }
}
