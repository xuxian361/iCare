package com.sundy.icare.net;

/**
 * Created by sundy on 15/12/6.
 */
public class MyURL {

    /**
     * URL Utils
     */
    public static final String HTTP_DEV = "http://192.168.1.109/api/"; //本地
    public static final String HTTP_UAT = "http://112.74.124.251/api/"; //网络

    public static final String HTTP_BASE = MyURL.HTTP_UAT;

    /*----------------------------------------------------------------------------------------*/
    /**
     * 登陆
     */
    public static final String URL_LOGIN = HTTP_BASE + "member/login";

    /**
     * 注册
     */
    public static final String URL_REGISTRATION = HTTP_BASE + "member/registration";

    /**
     * 发送手机验证码
     */
    public static final String URL_SEND_SMS_CODE = HTTP_BASE + "member/sendSmsCode";

    /**
     * 检查手机验证码
     */
    public static final String URL_CHECK_SMS_CODE = HTTP_BASE + "member/checkSmsCode";

    /**
     * 登出
     */
    public static final String URL_LOGOUT = HTTP_BASE + "member/logout";

    /**
     * 忘记密码
     */
    public static final String URL_FORGET_PASSWORD = HTTP_BASE + "member/forgetPassword";

    /**
     * 修改密码
     */
    public static final String URL_CHANGE_PASSWORD = HTTP_BASE + "member/changePassword";

    /**
     * 发送邮箱验证码
     */
    public static final String URL_SEND_EMAIL_CODE = HTTP_BASE + "member/sendEmailCode";

    /**
     * 绑定邮箱
     */
    public static final String URL_BIND_MAILBOX = HTTP_BASE + "member/bindMailbox";

    /**
     * 获取我的账号
     */
    public static final String URL_GET_MY_ACCOUNT = HTTP_BASE + "member/myAccount";

    /**
     * 获取用户资料
     */
    public static final String URL_GET_MEMBER_PROFILE = HTTP_BASE + "member/memberProfile";


}
