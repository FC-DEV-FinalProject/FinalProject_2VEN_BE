package com.sysmatic2.finalbe.exception;

public class TradingTypeNotFoundException extends RuntimeException {
    public TradingTypeNotFoundException(Integer id) {
        super("매매유형이 존재하지 않습니다. ID: " + id);
    }
}
