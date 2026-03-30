package com.ruoyi.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * etcd 配置属性
 *
 * @author ruoyi
 */
@Component
@ConfigurationProperties(prefix = "etcd")
public class EtcdProperties
{
    /**
     * 是否启用 etcd 同步功能
     */
    private boolean enabled = true;

    /**
     * etcd 服务器地址（支持多个，逗号分隔）
     */
    private String endpoints = "http://127.0.0.1:2379";

    /**
     * etcd key 前缀
     */
    private String prefix = "/dsp/config";

    /**
     * 连接超时时间（秒）
     */
    private int connectTimeout = 5;

    /**
     * 请求超时时间（秒）
     */
    private int requestTimeout = 10;

    /**
     * 认证配置
     */
    private Auth auth = new Auth();

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getEndpoints()
    {
        return endpoints;
    }

    public void setEndpoints(String endpoints)
    {
        this.endpoints = endpoints;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public int getConnectTimeout()
    {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout)
    {
        this.connectTimeout = connectTimeout;
    }

    public int getRequestTimeout()
    {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout)
    {
        this.requestTimeout = requestTimeout;
    }

    public Auth getAuth()
    {
        return auth;
    }

    public void setAuth(Auth auth)
    {
        this.auth = auth;
    }

    /**
     * 认证配置
     */
    public static class Auth
    {
        /**
         * 是否开启认证
         */
        private boolean enabled = false;

        /**
         * 用户名
         */
        private String user = "";

        /**
         * 密码
         */
        private String password = "";

        public boolean isEnabled()
        {
            return enabled;
        }

        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }

        public String getUser()
        {
            return user;
        }

        public void setUser(String user)
        {
            this.user = user;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }
}
