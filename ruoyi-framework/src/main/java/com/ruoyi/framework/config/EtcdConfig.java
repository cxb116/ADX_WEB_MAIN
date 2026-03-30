package com.ruoyi.framework.config;

import com.ruoyi.common.config.properties.EtcdProperties;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ByteSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * etcd 配置类
 *
 * @author ruoyi
 */
@Configuration
@ConditionalOnProperty(prefix = "etcd", name = "enabled", havingValue = "true")
public class EtcdConfig
{
    @Autowired
    private EtcdProperties etcdProperties;

    /**
     * 创建 etcd Client Bean
     */
    @Bean
    public Client etcdClient()
    {
        // 如果开启了认证
        if (etcdProperties.getAuth().isEnabled())
        {
            return Client.builder()
                    .endpoints(etcdProperties.getEndpoints().split(","))
                    .connectTimeout(Duration.ofSeconds(etcdProperties.getConnectTimeout()))
                    .user(ByteSequence.from(etcdProperties.getAuth().getUser().getBytes()))
                    .password(ByteSequence.from(etcdProperties.getAuth().getPassword().getBytes()))
                    .build();
        }
        else
        {
            return Client.builder()
                    .endpoints(etcdProperties.getEndpoints().split(","))
                    .connectTimeout(Duration.ofSeconds(etcdProperties.getConnectTimeout()))
                    .build();
        }
    }
}
