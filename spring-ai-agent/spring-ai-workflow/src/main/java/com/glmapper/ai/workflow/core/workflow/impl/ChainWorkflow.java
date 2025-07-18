package com.glmapper.ai.workflow.core.workflow.impl;

import com.glmapper.ai.workflow.core.step.WorkflowStep;
import com.glmapper.ai.workflow.core.workflow.Workflow;
import com.glmapper.ai.workflow.entity.req.WorkflowRequest;
import com.glmapper.ai.workflow.entity.resp.WorkflowResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName ChainWorkflow
 * @Description “ 串行/链式工作流 ” 实现：按顺序执行一系列工作流步骤，前一步骤的输出作为后一步骤的输入
 * @Author masen.27
 * @Date 2025/7/18 13:12
 * @Version 1.0
 */
@Slf4j
// 实现了一个链式工作流（ChainWorkflow），它将多个工作流步骤（WorkflowStep）按顺序串联执行，前一个步骤的输出作为后一个步骤的输入，形成流水线式处理
public class ChainWorkflow implements Workflow {
    private final List<WorkflowStep> steps; // 存储工作步骤（实现了WorkflowStep的具体工作步骤）的列表

    public ChainWorkflow(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    // 所有步骤执行完成后，将最终输出封装到 WorkflowResponse 中返回
    @Override
    public WorkflowResponse execute(WorkflowRequest input) {
        Object currentInput = input.getQuestion();

        try{
            log.info("开始执行链式工作流，步骤数量：{}",steps.size());

            for(WorkflowStep step : steps){
                log.info("执行步骤: {}，模型输入：{}",step.name(),currentInput);
                currentInput = step.execute(currentInput); // 前一步的输出作为下一步的输入
            }

            log.info("链式工作流执行完成");
            return WorkflowResponse.builder()
                    .content(currentInput!=null?currentInput.toString():null)
                    .success(true)
                    .build();
        }catch (Exception e){
            log.error("链式工作流执行失败", e);
            return WorkflowResponse.builder()
                    .success(false)
                    .errorMessage("工作流执行失败: " + e.getMessage())
                    .build();
        }

    }
}
