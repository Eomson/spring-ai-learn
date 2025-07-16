package com.glmapper.ai.chat.ollama.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName OllamaChatClientConfig
 * @Description Ollama配置文件
 * @Author masen.27
 * @Date 2025/7/16 18:58
 * @Version 1.0
 */
@Configuration
public class OllamaChatClientConfig {
    @Bean
    public ChatClient chatClient(OllamaChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("你是一个幽默的聊天机器人，总是以幽默的口吻回答用户问题")
                .build();
    }
}
