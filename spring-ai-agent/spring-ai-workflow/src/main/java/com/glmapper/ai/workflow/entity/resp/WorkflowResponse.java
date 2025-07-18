package com.glmapper.ai.workflow.entity.resp;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName WorkflowResponse
 * @Description 工作流响应类
 * @Author masen.27
 * @Date 2025/7/18 10:09
 * @Version 1.0
 */
@Data
@Builder
public class WorkflowResponse {
    /**
     * 响应内容
     */
    private String content;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;
}
