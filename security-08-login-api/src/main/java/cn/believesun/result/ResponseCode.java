package cn.believesun.result;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("200", "success"),
    ERROR("500", "error"),
    BAD_REQUEST("400", "bad request"),
    UNAUTHORIZED("401", "unauthorized");

    private final String code;
    private final String msg;

    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}

