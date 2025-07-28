package com.glmapper.ai.mcp.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

/**
 * @ClassName WeatherServiceServer
 * @Description TODO
 * @Author masen.27
 * @Date 2025/7/25 18:49
 * @Version 1.0
 */
@Slf4j
@Service
public class WeatherServiceServer {

    @Tool(name = "getCurrentWeather", description = "查询指定城市当前天气")
    public String getCurrentWeather(@ToolParam(description = "城市名称") String cityName) {
        log.info("当前城市:{}", cityName);
        return "天气大风";
    }
}
