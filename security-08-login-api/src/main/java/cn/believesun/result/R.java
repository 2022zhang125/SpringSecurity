package cn.believesun.result;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class R {
    private String msg;
    private String code;
    private Object data;

    public static R SUCCESS(Object data) {
        return R.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .msg(ResponseCode.SUCCESS.getMsg())
                .data(data)
                .build();
    }
    public static R SUCCESS() {
        return SUCCESS(null);
    }

    public static R ERROR(String msg, String code) {
        return R.<Void>builder().msg(msg).code(code).build();
    }

    public static R ERROR() {
        return ERROR(ResponseCode.ERROR.getMsg(), ResponseCode.ERROR.getCode());
    }
}
