package com.glmapper.ai.tc.tools.tool;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * @ClassName DatrTimeTools
 * @Description 时间工具
 * @Author masen.27
 * @Date 2025/7/23 17:28
 * @Version 1.0
 */
public class DateTimeTools {

    @Tool(description = "获取当前用户所在地区的日期和时间")
    // 例如用户问：“现在是什么时间？”“当前日期和时间是多少？”
    // 无参数，直接调用即可 返回默认格式的字符串（如 2025-07-24T18:30:00+08:00[Asia/Shanghai]），包含时区信息
    public String getCurrentDateTime(){
        // LocaleContextHolder.getTimeZone()：通过 Spring 的LocaleContextHolder获取当前用户的时区（通常从请求上下文或会话中获取）
        // toZoneId()：将TimeZone对象转换为 Java 8 + 的ZoneId对象
        // atZone(ZoneId)：将本地日期时间转换为指定时区的ZonedDateTime
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "获取当前用户所在地区的日期和时间")
    // 例如用户问：“请用 yyyy 年 MM 月 dd 日 HH:mm 格式显示现在时间”“当前时间用 MM/dd/yyyy 怎么表示？”
    // 必须传入 ToolContext 参数，且需要从上下文获取 format 格式（如 "yyyy-MM-dd HH:mm:ss"）
    // 返回用户指定格式的字符串（如用户要求 "MM/dd/yyyy"，则返回 07/24/2025）
    public String getFormatDateTime(ToolContext toolContext){
        return new SimpleDateFormat(toolContext.getContext().get("format").toString())
                .format(LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toInstant().toEpochMilli());
    }

}


