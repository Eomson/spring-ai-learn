package com.glmapper.ai.workflow.core.workflow.impl;

import com.glmapper.ai.workflow.core.step.WorkflowStep;
import com.glmapper.ai.workflow.core.workflow.Workflow;
import com.glmapper.ai.workflow.entity.req.WorkflowRequest;
import com.glmapper.ai.workflow.entity.resp.WorkflowResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @ClassName RoutingWorkflow
 * @Description 路由工作流实现：基于路由规则选择合适的工作流步骤执行，动态选择一个子步骤执行，需根据输入选择不同处理逻辑	---> 多意图对话机器人、动态业务路由
 * @Author masen.27
 * @Date 2025/7/18 13:57
 * @Version 1.0
 */
@Slf4j
// 实现了一个路由工作流（RoutingWorkflow），它的核心功能是 根据输入动态选择合适的子工作流步骤执行，类似于 “决策树” 或 “路由分发器”
public class RoutingWorkflow implements Workflow {

    // 一个 WorkflowStep 实现类（通常是之前提到的 RouterSelectorWorkflowStep），负责根据输入（如用户问题）分析并返回一个路由键（routeKey），决定下一步该执行哪个子步骤
    private final WorkflowStep routerSelector; // 路由选择器（负责决策用哪个步骤）
    private final Map<String, WorkflowStep> stepMap; // 路由键与子步骤的映射表

    public RoutingWorkflow(WorkflowStep routerSelector, Map<String, WorkflowStep> stepMap) {
        this.routerSelector = routerSelector;
        this.stepMap = stepMap;
    }

    @Override
    public WorkflowResponse execute(WorkflowRequest input) {

        try {
            log.info("开始执行路由工作流, 路由规则数量: {}", stepMap.size());

            // 1. 使用路由选择器确定最合适的路由键
            //路由决策（选择子步骤）
            // 调用 routerSelector.execute(input) 分析输入（如用户问题），得到一个路由键（routeKey）。例如：
            // 用户输入 “翻译这句话” → 路由选择器返回 routeKey = "translation"。
            // 用户输入 “总结这段文字” → 路由选择器返回 routeKey = "summarization"
            String routeKey = (String) routerSelector.execute(input);
            log.info("路由结果: {}", routeKey);

            // 2. 根据路由键查找对应的子步骤
            // 根据 routeKey 从 stepMap 中获取对应的 WorkflowStep。例如：stepMap.get("translation") → 返回翻译子步骤（TranslationStep）
            WorkflowStep step = stepMap.get(routeKey);

            // 3. 校验子步骤是否存在
            if (step == null) {
                log.error("未找到路由对应的步骤: {}", routeKey);
                return WorkflowResponse.builder()
                        .success(false)
                        .errorMessage("未找到路由对应的步骤: " + routeKey)
                        .build();
            }

            // 4. 执行找到的子步骤
            log.info("执行步骤: {}", step.name());
            Object result = step.execute(input.getQuestion());

            log.info("路由工作流执行完成, 执行结果为: \n {}", result);

            // 5. 返回执行结果
            return WorkflowResponse.builder()
                    .content(result != null ? result.toString() : null)
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("路由工作流执行失败", e);
            return WorkflowResponse.builder()
                    .success(false)
                    .errorMessage("工作流执行失败: " + e.getMessage())
                    .build();
        }
    }
}
