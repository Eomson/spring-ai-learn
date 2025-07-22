package com.glmapper.ai.evaluation.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @ClassName EvaluationService
 * @Description TODO
 * @Author masen.27
 * @Date 2025/7/21 18:39
 * @Version 1.0
 */
@Service
// 定义了一个用于评估 AI 模型响应与上下文相关性的服务类 EvaluationService，核心功能是判断模型生成的回答是否与提供的上下文信息一致
public class EvaluationService {

    //  RelevancyEvaluator 内部内置了自己的评估提示词
    private static String prompt = "Your task is to evaluate if the response for the query\n" + "is in line with the context information provided.\n" + "\n" + "You have two options to answer. Either YES or NO.\n" + "\n" + "Answer YES, if the response for the query\n" + "is in line with context information otherwise NO.\n" + "\n" + "Query:\n" + "{query}\n" + "\n" + "Response:\n" + "{response}\n" + "\n" + "Context:\n" + "{context}\n" + "\n" + "Answer:";

    @Autowired
    private ChatClient ollamaChatClient; // Ollama模型客户端

    @Autowired
    private ChatClient qwenChatClient; // 千问模型客户端

    @Autowired
    private OpenAiChatModel qwenChatModel;

    /**
     * 评估消息内容
     * @param message
     * @return 该方法接收一个输入消息，完成 “生成响应→评估响应” 的全流程，返回评估结果
     */
    public EvaluationResponse evaluate(String message) {
        // 使用ollama 模型进行评估
        // 调用 ollamaChatClient（Ollama 客户端），以输入 message 作为用户问题，生成响应内容 openAiResponse
        String ollamaResponse = ollamaChatClient.prompt().user(message).call().content();
        String question = message;
        EvaluationRequest evaluationRequest = new EvaluationRequest(
                question, // 原始问题
                Collections.emptyList(), // 空的上下文列表
                ollamaResponse); // 待评估的响应（Ollama模型生成的结果）
        // 创建相关性评估器，使用qwenChatModel作为评估模型
        RelevancyEvaluator evaluator = new RelevancyEvaluator(ChatClient.builder(this.qwenChatModel));
        // 执行评估，得到结果
        EvaluationResponse evaluationResponse = evaluator.evaluate(evaluationRequest);
        return evaluationResponse;
    }

}
