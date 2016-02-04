package com.sundy.huanxin;

/**
 * Created by sundy on 16/2/4.
 */
public class EaseConstant {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
//    /**
//     * app自带的动态表情，直接去resource里获取图片
//     */
//    public static final String MESSAGE_ATTR_BIG_EXPRESSION_ICON = "em_big_expression_icon";
//    /**
//     * 动态下载的表情图片，需要知道表情url
//     */
//    public static final String MESSAGE_ATTR_BIG_EXPRESSION_URL = "em_big_expression_url";


    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;

    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";
}
