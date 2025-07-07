package cn.believesun.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class R {
    private String msg;
    private Integer code;
    private Object data;

    public static R SUCCESS(String msg,Integer code,Object data) {
        return R.builder()
                .code(code)
                .msg(msg)
                .data(data)
                .build();
    }
    public static R FAIL(String msg,Integer code) {
        return R.builder()
                .msg(msg)
                .code(code)
                .build();
    }
}
