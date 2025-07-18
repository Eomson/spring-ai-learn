package com.glmapper.ai.workflow.core.step.impl;

import com.glmapper.ai.workflow.core.step.WorkflowStep;
import com.glmapper.ai.workflow.entity.req.WorkflowRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import javax.validation.constraints.Null;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName RouterSelectorWorkflowStep
 * @Description AI路由选择器实现WorkflowStep接口，用于根据用户问题选择合适的路由
 * @Author masen.27
 * @Date 2025/7/18 10:58
 * @Version 1.0
 */
@Slf4j
// 实现了一个 路由选择工作流步骤（RouterSelectorWorkflowStep），
// 它通过大语言模型（LLM）智能地将用户请求路由到合适的子工作流步骤。这是一个典型的基于 AI 的决策组件，常用于复杂工作流中的分支控制
public class RouterSelectorWorkflowStep implements WorkflowStep {

    private final ChatClient chatClient; // 调用大语言模型的客户端
    private final Map<String, WorkflowStep> stepMap; // 路由映射表 -- 键为路由名称（如 "translation"、"summarization"），值为对应的工作流步骤（WorkflowStep）
    private final String name; // 步骤名称


    public RouterSelectorWorkflowStep(ChatClient chatClient, Map<String, WorkflowStep> stepMap, String name) {
        this.chatClient = chatClient;
        this.stepMap = stepMap;
        this.name = name;
    }

    // 要求模型仅返回路由键名，避免生成额外文本
    private static final String PROMPT_TEMPLATE = """
            你是一个专业的路由选择器。根据用户的问题，从以下可用的路由中选择最合适的一个：
            可用路由:
            %s \s
            请仅返回最合适的路由的键名，不要包含任何额外解释。例如，如果最合适的路由是"technical"，只需返回"technical"。 
            """;

    /**
     * 执行步骤
     * @param input 输入数据
     * @return 步骤执行结果
     */
    @Null
    @Override
    public Object execute(Object input) {
        if (!(input instanceof WorkflowRequest)) { // 用户输入不是WorkflowRequest类型的
            throw new IllegalArgumentException("Input must be of type WorkflowRequest");
        }

        WorkflowRequest request = (WorkflowRequest) input;

        // 构建提示文本
        String  routeInfo = stepMap.entrySet().stream() // Map<String, WorkflowStep> stepMap  值是工作步骤jiekou（工作执行步骤、步骤名称）
                .map(entry -> "- "+entry.getKey() + ": "+ entry.getValue().name())
                .collect(Collectors.joining("\n"));

        String promptTemplate = String.format(PROMPT_TEMPLATE, routeInfo);

        // 创建并发送提示
        Prompt prompt = new Prompt(
                new SystemMessage(promptTemplate),
                new UserMessage(request.getQuestion())
        );

        String routeKey = chatClient.prompt(prompt).call().content().trim();

        // 确保获取到的是有效的路由
        if(!stepMap.containsKey(routeKey)) {
            return null;
        }
        return routeKey;
    }

    @Override
    public String name() {
        return name;
    }
}
