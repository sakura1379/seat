package com.zlr.seat.entity.enums;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.common.enums
 * @Description
 * @create 2022-09-15-下午5:32
 */
public enum ResultStatus {
    SUCCESS(0, "成功"),
    FAILD(-1, "失败"),
    EXCEPTION(-1, "系统异常"),
    PARAM_ERROR(10000, "参数错误"),
    SYSTEM_ERROR(10001, "系统错误"),
    FILE_NOT_EXIST(10002, "文件不存在"),
    FILE_NOT_DOWNLOAD(10003, "文件没有下载"),
    FILE_NOT_GENERATE(10004, "文件没有生成"),
    FILE_NOT_STORAGE(10005, "文件没有入库"),
    SYSTEM_DB_ERROR(10006, "数据库系统错误"),
    FILE_ALREADY_DOWNLOAD(10007, "文件已经下载"),
    DATA_ALREADY_PEXISTS(10008, "数据已经存在"),
    PERMISSION_DENIED(10009,"无权限访问"),
    CREDENTIALS_INVALID(10010,"凭证无效或已过期"),
    FILE_NOT_DELETE(10011, "文件删除失败"),

    /**
     * 注册登录
     */
    RESIGETR_SUCCESS(20000, "注册成功!"),
    RESIGETER_FAIL(200001, "注册失败!"),
    CODE_FAIL(200002, "验证码不一致!"),
    MOBILE_EXIST(200003, "手机号已存在!"),
    NAME_EXIST(200004, "用户名已存在!"),
    OLD_PASSWORD_ERROR(200005, "原密码错误!"),
    MOBILE_NOT_MATCH(200006, "手机号与当前用户手机号不匹配!"),
    MOBILE_NOT_CHECK(200006, "未经原手机号验证或验证已超时，请验证原手机号通过后再试"),

    /**
     * check
     */
    BIND_ERROR(30001, "参数校验异常：%s"),
    ACCESS_LIMIT_REACHED(30002, "请求非法!"),
    REQUEST_LIMIT(30004, "访问太频繁!"),
    SESSION_ERROR(30005, "Session不存在或者已经失效!"),
    PASSWORD_EMPTY(30006, "登录密码不能为空!"),
    MOBILE_EMPTY(30007, "手机号不能为空!"),
    MOBILE_ERROR(30008, "手机号格式错误!"),
    MOBILE_NOT_EXIST(30009, "手机号不存在!"),
    PASSWORD_ERROR(30010, "密码错误!"),
    USER_NOT_EXIST(30011, "用户不存在！"),
    THIRD_REQUEST_ERROR(50001,"第三方接口请求错误"),
    THIRD_TOKEN_ERROR(50002,"获取第三方token出错"),
    CLIENT_ERROR(50003, "无效客户端"),
    TYPE_ERROR(50004,"无效type"),
    REFRESH_CREDENTIALS_INVALID(50005,"刷新凭证无效或已过期"),
    SMS_ERROR(50006,"短信发送失败"),
    SMS_WRONG(50006,"验证码不正确"),


    /**
     * 预订座位模块
     */
    ORDER_NOT_EXIST(60001, "订单不存在"),
    ORDER_OVER(40001, "座位已经预订完毕"),
    REPEATE_ORDER(40002, "不能重复预订"),
    ORDER_FAIL(40003, "预订失败");

    private int code;
    private String message;

    private ResultStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private ResultStatus(Object... args) {
        this.message = String.format(this.message, args);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return this.name();
    }

    public String getOutputName() {
        return this.name();
    }

    public String toString() {
        return this.getName();
    }
}
