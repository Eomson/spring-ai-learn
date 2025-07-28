package com.glmapper.ai.mcp.server.config;

import com.glmapper.ai.mcp.server.service.WeatherServiceServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName McpServerConfiguration
 * @Description MCP配置类
 * @Author masen.27
 * @Date 2025/7/25 18:42
 * @Version 1.0
 */
@Configuration
public class McpServerConfiguration {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(WeatherServiceServer weatherServer) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(weatherServer)
                .build();
    }
}
