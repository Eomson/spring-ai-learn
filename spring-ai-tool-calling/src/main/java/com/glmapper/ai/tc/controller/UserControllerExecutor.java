package com.glmapper.ai.tc.controller;

import com.glmapper.ai.tc.tools.tool.DateTimeTools;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserControllerExecutor
 * @Description TODO
 * @Author masen.27
 * @Date 2025/7/23 18:38
 * @Version 1.0
 */
@Component
// 用于手动处理用户消息并执行工具调用流程，核心功能是将用户输入传递给 AI 模型，根据模型判断自动调用工具（如时间工具），并最终返回处理后的回答
public class UserControllerExecutor {

    @Autowired
    private OllamaChatModel chatModel;

    // 该方法接收用户消息（message），完成 “用户输入 → AI 处理 → 工具调用（如有）→ 最终回答” 的完整流程，返回 AI 生成的最终结果
    public String manualExecTools(String message) {
        // 创建一个 ToolCallingManager 实例 --- 创建工具调用管理器，用于执行 AI 模型发起的工具调用
        ToolCallingManager toolCallingManager = ToolCallingManager.builder().build();
        // 注册工具方法   将 DateTimeTools 类中的工具方法转换为 ToolCallback 数组（工具回调）
        // 会自动扫描 DateTimeTools 中所有带 @Tool 注解的方法，将其转换为 ToolCallback 数组（标准化的工具回调对象）
        ToolCallback[] toolCallbacks = ToolCallbacks.from(new DateTimeTools());
        // 创建一个 ChatOptions 实例，包含工具调用选项
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallbacks)
                .internalToolExecutionEnabled(false) // 设置为 false 表示 “不允许 AI 模型自己执行工具”，而是由当前代码（toolCallingManager）手动执行（便于控制流程）
                .build();
        // 创建一个 Prompt 实例，包含用户消息和工具调用选项
        Prompt prompt = new Prompt(message, chatOptions);
        // 调用 ChatModel 进行对话
        ChatResponse chatResponse = chatModel.call(prompt);
        // 如果 ChatResponse 包含工具调用，则执行工具调用
        while (chatResponse.hasToolCalls()) {
            // 1. 执行工具调用：由 toolCallingManager 处理 AI 提出的工具调用请求
            ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, chatResponse);
            // 2. 更新请求：将工具执行结果（如“当前时间是 15:30”）加入对话历史，生成新的 Prompt
            prompt = new Prompt(toolExecutionResult.conversationHistory(), chatOptions);
            // 3. 再次调用 AI 模型：基于工具结果生成最终回答
            chatResponse = chatModel.call(prompt);
        }
        // 获取最终的回答
        String answer = chatResponse.getResult().getOutput().getText();
        System.out.println(answer);
        return answer;
    }


}
