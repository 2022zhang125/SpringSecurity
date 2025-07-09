package cn.believesun.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class R {
    private String msg;
    private Integer code;
    private Object data;

    public static R SUCCESS(String msg, Object data) {
        return R.builder()
                .msg(msg)
                .code(200)
                .data(data)
                .build();
    }

    public static R FAIL(String msg){
        return R.builder()
                .msg(msg)
                .code(500)
                .data(null)
                .build();
    }
}
