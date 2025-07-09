package cn.believesun.constant;

public class Constant {
    public static final String SECRET = "adskj!@&UDF>;;l";

    // redis的Hash大Key的命名规范：项目名:模块名:功能名[:业务唯一参数]
    public static final String REDIS_TOKEN_KEY = "security:user:login";
}
