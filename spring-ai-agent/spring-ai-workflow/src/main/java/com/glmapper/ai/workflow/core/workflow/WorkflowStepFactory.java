package com.glmapper.ai.workflow.core.workflow;

import com.glmapper.ai.workflow.core.step.WorkflowStep;
import com.glmapper.ai.workflow.core.step.impl.DefaultWorkflowStep;
import com.glmapper.ai.workflow.core.step.impl.RouterSelectorWorkflowStep;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName WorkflowStepFactory
 * @Description 提供创建AI工作流步骤的功能
 * @Author masen.27
 * @Date 2025/7/18 14:16
 * @Version 1.0
 */
@Service
@Slf4j
@AllArgsConstructor
public class WorkflowStepFactory {

    private final ChatClient chatClient;

    /**
     * 创建AI工作流步骤
     * @param name 步骤名称
     * @param promptTemplate 提示词模板
     * @return 工作流步骤
     */
    public WorkflowStep createAiStep(String name, String promptTemplate) {
        return new DefaultWorkflowStep(name, promptTemplate, chatClient);
    }

    /**
     * 创建AI路由选择器
     * @param name 步骤名称
     * @param stepMap 步骤映射
     * @return AI路由选择器
     */
    public RouterSelectorWorkflowStep createAiRouterSelector(String name, Map<String, WorkflowStep> stepMap) {
        return new RouterSelectorWorkflowStep(chatClient, stepMap, name);
    }
}
