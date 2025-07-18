package com.glmapper.ai.workflow.core.step.impl;

import com.glmapper.ai.workflow.core.step.WorkflowStep;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import javax.sound.midi.SysexMessage;

/**
 * @ClassName DefaultWorkflowStep
 * @Description 默认工作流步骤  --- 多个 DefaultWorkflowStep 可以组合成复杂的工作流  --- 直接询问直接回答
 * @Author masen.27
 * @Date 2025/7/18 10:25
 * @Version 1.0
 */
// 定义了一个名为 DefaultWorkflowStep 的记录类（Record Class），它实现了 WorkflowStep 接口，用于表示工作流中的一个处理步骤
// Record 类：
// 1、Java 14+ 引入的特殊类，用于简洁地定义不可变数据类（自动生成构造器、getter、equals、hashCode 等方法）。
// 2、这里的 name、promptTemplate、chatClient 是类的组件（components），自动生成对应的访问器方法（如 name()、promptTemplate()）
public record DefaultWorkflowStep(String name, String promptTemplate, ChatClient chatClient) implements WorkflowStep {
    /**
     * 执行本步骤
     * @param input 输入数据
     * @return 步骤执行结果
     */
    @Override
    public Object execute(Object input) {
        String inputStr = input != null ? input.toString() : "";
        Prompt prompt = new Prompt(
                new SystemMessage(promptTemplate),
                new UserMessage(inputStr)
        );

        return chatClient.prompt(prompt).call().content();
    }
}
