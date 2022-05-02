package com.nowcoder.community.service;

public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS=0;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE=2;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT=1;
    /**
     * 默认状态的登录凭证超时时间
     */
    int DEFAULT_SECONDS=3600*12;
    /**
     * 记住状态的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECONDS=3600*24;
}
