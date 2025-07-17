package com.glmapper.ai.chat.memory.jdbc.service;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname StorageService
 * @Description StorageService
 * @Date 2025/5/29 09:29
 * @Created by glmapper
 */
@Service
public class ChatMemoryService {

    @Autowired // 注入聊天模型（如OpenAI、讯飞等大语言模型接口）
    private ChatModel chatModel;

    @Autowired // 注入JDBC聊天内存仓库（用于持久化对话记录）
    private JdbcChatMemoryRepository chatMemoryRepository;

    @Getter
    private ChatMemory chatMemory; // 对话内存对象，用于管理单轮/多轮对话

    @PostConstruct  // 在对象构造后自动执行，初始化对话内存
    public void init() {
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository) // 将对话记忆关联持久化仓库
                .maxMessages(20) // 设置对话窗口最大消息数（超过则自动丢弃旧消息
                .build();
    }


    public String call(String message, String conversationId) {
        // 1. 将用户输入转换为UserMessage对象（标记为“用户发送”）
        UserMessage userMessage = new UserMessage(message);
        // 2. 将用户消息存入对话内存（并通过JDBC持久化到数据库）
        this.chatMemory.add(conversationId, userMessage);
        // 3. 获取当前对话的完整历史（根据conversationId查询）
        List<Message> messages = chatMemory.get(conversationId);
        // 4. 调用大语言模型，传入对话历史生成回答
        ChatResponse response = chatModel.call(new Prompt(messages));
        // 5. 将模型的回答存入对话内存（持久化到数据库）
        chatMemory.add(conversationId, response.getResult().getOutput());
        // 6. 返回模型回答的文本内容
        return response.getResult().getOutput().getText();
    }

}


