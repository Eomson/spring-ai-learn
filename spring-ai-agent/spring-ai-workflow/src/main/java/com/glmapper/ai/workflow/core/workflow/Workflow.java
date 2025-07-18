package com.glmapper.ai.workflow.core.workflow;

import com.glmapper.ai.workflow.entity.req.WorkflowRequest;
import com.glmapper.ai.workflow.entity.resp.WorkflowResponse;

/**
 * @InterfaceName Workflow
 * @Description 工作流核心接口
 * @Author masen.27
 * @Date 2025/7/18 13:10
 * @Version 1.0
 */
public interface Workflow {

    /**
     * 执行本工作流
     * @param request 输入的请求
     * @return 工作流执行结果
     */
    WorkflowResponse execute(WorkflowRequest request);
}
