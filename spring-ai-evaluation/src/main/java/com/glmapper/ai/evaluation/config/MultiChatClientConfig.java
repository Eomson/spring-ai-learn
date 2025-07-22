package com.glmapper.ai.evaluation.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MultiChatClientConfig
 * @Description TODO
 * @Author masen.27
 * @Date 2025/7/21 18:25
 * @Version 1.0
 */
@Configuration
public class MultiChatClientConfig {

    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("你是一个友好的聊天机器人，总是以json的形式回答用户问题")
                .build();
    }

    @Bean
    public ChatClient qwenChatClient(OpenAiChatModel qwenChatModel) {
        return ChatClient.create(qwenChatModel);
    }
}
