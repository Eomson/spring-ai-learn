package com.glmapper.ai.workflow.entity.req;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName WorkflowRequest
 * @Description 工作流请求类
 * @Author masen.27
 * @Date 2025/7/18 10:07
 * @Version 1.0
 */
@Data
@Builder
public class WorkflowRequest {
    /**
     * 请求内容
     */
    private String question;
}
