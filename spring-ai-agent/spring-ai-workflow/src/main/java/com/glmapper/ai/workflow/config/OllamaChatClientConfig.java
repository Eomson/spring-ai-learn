package com.glmapper.ai.workflow.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName OllamaChatClientConfig
 * @Description ollama模型配置类
 * @Author masen.27
 * @Date 2025/7/18 10:02
 * @Version 1.0
 */
@Configuration
public class OllamaChatClientConfig {

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("你是一擅长将笑话的聊天机器人")
                .build();
    }
}
