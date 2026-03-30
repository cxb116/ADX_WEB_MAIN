package com.ruoyi.common.core.etcd;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.ruoyi.common.config.properties.EtcdProperties;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ByteSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * etcd 操作工具类
 *
 * @author ruoyi
 */
@Component
@ConditionalOnProperty(prefix = "etcd", name = "enabled", havingValue = "true")
public class EtcdTemplate
{
    private static final Logger log = LoggerFactory.getLogger(EtcdTemplate.class);

    @Autowired
    private Client client;

    @Autowired
    private EtcdProperties properties;

    /**
     * 同步数据到 etcd
     *
     * @param type 数据类型 (dsp/company/launch)
     * @param action 操作类型 (add/update/delete)
     * @param id 数据ID
     * @param data 数据对象（add/update 时需要，delete 时可为 null）
     */
    public void sync(String type, String action, Long id, Object data)
    {
        if (!properties.isEnabled())
        {
            log.debug("etcd 同步功能已关闭，跳过操作: type={}, action={}, id={}", type, action, id);
            return;
        }

        long startTime = System.currentTimeMillis();
        String key = buildKey(type, action, id);

        log.info("开始 etcd 同步操作: type={}, action={}, id={}, key={}", type, action, id, key);

        try
        {
            if ("delete".equals(action))
            {
                // 删除操作
                log.debug("执行 etcd 删除操作: key={}", key);
                client.getKVClient().delete(
                        ByteSequence.from(key, StandardCharsets.UTF_8)
                ).get(properties.getRequestTimeout(), TimeUnit.SECONDS);

                long duration = System.currentTimeMillis() - startTime;
                log.info("etcd 删除操作成功: type={}, action={}, id={}, key={}, 耗时={}ms",
                        type, action, id, key, duration);
            }
            else
            {
                // 添加/更新操作
                String value = serializeToJson(data);
                log.debug("执行 etcd 写入操作: key={}, value={}", key, truncateValue(value));

                client.getKVClient().put(
                        ByteSequence.from(key, StandardCharsets.UTF_8),
                        ByteSequence.from(value, StandardCharsets.UTF_8)
                ).get(properties.getRequestTimeout(), TimeUnit.SECONDS);

                long duration = System.currentTimeMillis() - startTime;
                log.info("etcd 写入操作成功: type={}, action={}, id={}, key={}, 数据长度={}字节, 耗时={}ms",
                        type, action, id, key, value.length(), duration);
            }
        }
        catch (Exception e)
        {
            long duration = System.currentTimeMillis() - startTime;
            log.error("etcd 同步操作失败: type={}, action={}, id={}, key={}, 耗时={}ms, 错误类型={}, 错误信息={}",
                    type, action, id, key, duration, e.getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    /**
     * 便捷方法：添加数据
     */
    public void syncAdd(String type, Long id, Object data)
    {
        sync(type, "add", id, data);
    }

    /**
     * 便捷方法：更新数据
     */
    public void syncUpdate(String type, Long id, Object data)
    {
        sync(type, "update", id, data);
    }

    /**
     * 便捷方法：删除数据
     */
    public void syncDelete(String type, Long id)
    {
        sync(type, "delete", id, null);
    }

    /**
     * 构建 etcd key
     * 格式：{prefix}/{type}/{id}
     * 示例：/dsp/config/dsp/123
     *
     * 统一 key 格式，移除 action，使得接收端只需监听一个前缀即可接收所有操作
     * - 添加/更新：使用 PUT 操作写入数据
     * - 删除：使用 DELETE 操作删除 key
     */
    private String buildKey(String type, String action, Long id)
    {
        return String.format("%s/%s/%d", properties.getPrefix(), type, id);
    }

    /**
     * 序列化为 JSON 字符串
     * 使用 FastJson2，确保输出 snake_case 格式
     * 注意：需要在 Domain 类的 getter 上使用 @JSONField 注解指定 snake_case 名称
     */
    private String serializeToJson(Object data)
    {
        if (data == null)
        {
            return "";
        }
        return JSON.toJSONString(data,
                JSONWriter.Feature.WriteMapNullValue,
                JSONWriter.Feature.NotWriteRootClassName
        );
    }

    /**
     * 截断过长的 JSON 值用于日志输出
     */
    private String truncateValue(String value)
    {
        if (value == null)
        {
            return "null";
        }
        int maxLength = 2000;
        if (value.length() <= maxLength)
        {
            return value;
        }
        return value.substring(0, maxLength) + "... (省略 " + (value.length() - maxLength) + " 字符)";
    }
}
