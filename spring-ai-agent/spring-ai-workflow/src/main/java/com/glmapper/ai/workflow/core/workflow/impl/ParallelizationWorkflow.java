package com.glmapper.ai.workflow.core.workflow.impl;

import com.glmapper.ai.workflow.core.step.WorkflowStep;
import com.glmapper.ai.workflow.core.workflow.Workflow;
import com.glmapper.ai.workflow.entity.req.WorkflowRequest;
import com.glmapper.ai.workflow.entity.resp.WorkflowResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @ClassName ParallelizationWorkflow
 * @Description “ 并行工作流 ” 实现：同时执行多个工作流步骤，所有步骤使用相同的输入，最终结果是所有步骤结果的集合
 * @Author masen.27
 * @Date 2025/7/18 13:26
 * @Version 1.0
 */
@Slf4j
// 实现了一个并行工作流（ParallelizationWorkflow），它能让多个工作流步骤（WorkflowStep）同时执行（而非按顺序），从而提高整体处理效率。这是针对需要多任务并行处理场景的优化设计
public class ParallelizationWorkflow implements Workflow {

    private final List<WorkflowStep> steps; // 并行执行的步骤列表（步骤之间通常是 无依赖关系）

    public ParallelizationWorkflow(List<WorkflowStep> steps) {
        this.steps = steps;
    }

    @Override
    public WorkflowResponse execute(WorkflowRequest input) {

        try{
            log.info("开始执行并行工作流, 步骤数量: {}", steps.size());
            // Java 并发工具 CompletableFuture 实现多个步骤的并行执行
            // 1. 为每个步骤创建异步任务
            // 存储 异步任务结果 的列表，每个异步任务会返回一个 Map.Entry<String, Object> 对象（键值对）
            List<CompletableFuture<Map.Entry<String, Object>>> futures = new ArrayList<>();

            // 为每个步骤创建一个异步任务
            for (WorkflowStep step : steps) {

                // 创建异步任务（CompletableFuture.supplyAsync）
                //   对每个 WorkflowStep，通过 CompletableFuture.supplyAsync 提交一个异步任务到线程池执行。
                //   每个任务执行 step.execute(input.getQuestion())，并将结果与步骤名称（step.name()）关联为 Map.Entry。
                //   所有异步任务的引用被收集到 futures 列表中。
                // 注：supplyAsync 默认使用 ForkJoinPool.commonPool() 作为线程池，也可自定义线程池提高可控性
                CompletableFuture<Map.Entry<String,Object>> future = CompletableFuture.supplyAsync(()->{
                    log.info("执行步骤: {}", step.name());
                    Object result = step.execute(input.getQuestion()); // 每个步骤使用相同的初始输入
                    return Map.entry(step.name(), result); // 用步骤名称关联结果
                });
                futures.add(future); // 收集所有异步任务
            }

            // 2. 等待所有异步任务完成
            // CompletableFuture.allOf(...) 接收多个 CompletableFuture 作为参数，返回一个新的 CompletableFuture，当 所有传入的任务都完成时 才会触发完成
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[0]) // 转换为数组，作为allOf的参数
            );

            // 3. 收集所有步骤的结果
            // allFutures.thenApply(...) 在所有任务完成后执行，通过流操作遍历 futures 列表，调用 CompletableFuture.join() 获取每个任务的结果（此时任务已完成，join 不会阻塞）
            CompletableFuture<Map<String,Object>> resultFuture = allFutures.thenApply(v ->
                    futures.stream()
                            .map(CompletableFuture::join) // 阻塞获取每个任务的结果（此时已完成）
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );

            // 4. 获取最终结果并格式化
            Map<String, Object> results = resultFuture.get(); // 阻塞等待所有结果收集完成
            String content = formatResults(results);  // 格式化结果为可读字符串
            log.info("并行工作流执行完成，结果数量: {}, 执行结果为：\n {}", results.size(), content);

            return WorkflowResponse.builder()
                    .success(true)
                    .content(content)
                    .build();

        }catch (Exception e){
            log.error("并行工作流执行失败", e);
            return WorkflowResponse.builder()
                    .success(false)
                    .errorMessage("工作流执行失败: " + e.getMessage())
                    .build();
        }
    }

    /**
     * 格式化结果为可读字符串
     * @param results 结果映射
     * @return 格式化的结果字符串
     */
    private String formatResults(Map<String, Object> results) {
        StringBuilder sb = new StringBuilder();
        sb.append("并行工作流执行结果:\n");

        results.forEach((key, value) -> {
            sb.append("步骤 [").append(key).append("]: \n");
            sb.append(value != null ? value.toString() : "null").append("\n\n");
        });

        return sb.toString();
    }
}
