package com.glmapper.ai.workflow.core.step;

/**
 * @InterfaceName WorkflowStep
 * @Description 工作流步骤接口
 * @Author masen.27
 * @Date 2025/7/18 10:24
 * @Version 1.0
 */
public interface WorkflowStep {
    /**
     * 执行步骤
     * @param input 输入数据
     * @return 步骤执行结果
     */
    Object execute(Object input);

    /**
     * 获取步骤名称
     * @return 步骤名称
     */
    String name();
}
