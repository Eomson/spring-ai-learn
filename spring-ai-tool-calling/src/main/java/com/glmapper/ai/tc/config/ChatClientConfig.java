package com.glmapper.ai.tc.config;

import com.glmapper.ai.tc.tools.req.WeatherRequest;
import com.glmapper.ai.tc.tools.service.WeatherService;
import com.glmapper.ai.tc.tools.tool.DateTimeTools;
import com.glmapper.ai.tc.tools.tool.FileReaderTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ChatClientConfig
 * @Description 模型客户端
 * @Author masen.27
 * @Date 2025/7/23 18:06
 * @Version 1.0
 */
@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel, ChatMemory chatMemory) {
        // Method method = ReflectionUtils.findMethod(DateTimeTools.class, "getCurrentDateTime");
        // ToolCallback toolCallback = MethodToolCallback.builder()
        //        .toolDefinition(ToolDefinition.builder().name("getCurrentDateTime")
        //                .description("Get the current date and time in the user's timezone")
        //                .build())
        //        .toolMethod(method)
        //        .build();
        // ChatClient chatClient = ChatClient.builder(chatModel)
        //        .defaultToolCallbacks(toolCallback)
        //        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
        //        .defaultSystem("You are deepseek chat bot, you answer questions in a concise and accurate manner.")
        //        .build();
        //
        //
        //
        // ToolCallback[] toolCallbacks = ToolCallbacks.from(new DateTimeTools(),new FileReaderTools());
        // ChatClient.builder(chatModel)
        //        .defaultToolCallbacks(toolCallbacks)
        //        .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
        //        .defaultSystem("You are deepseek chat bot, you answer questions in a concise and accurate manner.")
        //        .build();

        // 创建一个名为currentWeather的工具回调对象，关联到WeatherService服务，用于描述该工具的功能、输入类型等元信息 -- 回调对象仅负责描述和衔接
        // 第一个参数"currentWeather"：工具的唯一标识（ID），用于区分不同工具（类似方法名或接口名）。
        // 第二个参数new WeatherService()：工具的实际实现对象（服务类），即真正提供 “获取天气” 功能的业务逻辑载体
        // 通过 ToolCallback 声明的工具回调（currentWeather）和 DateTimeTools 类中用 @Tool 注解标记的方法，本质上都是为了向聊天客户端注册可调用的工具
        ToolCallback toolCallback = FunctionToolCallback
                .builder("currentWeather",new WeatherService()) // 绑定实现
                .description("获取当前地区的天气")
                .inputType(WeatherRequest.class) // 指定该工具接收的输入参数类型为WeatherRequest类。
                .build();

        return ChatClient.builder(ollamaChatModel)
                // defaultTools(new DateTimeTools()) 会自动注册 DateTimeTools 中所有带 @Tool 注解的方法，而 defaultToolCallbacks(toolCallback) 则手动注册了
                // currentWeather 工具，两者最终都会被 ChatClient 识别为可用工具，供 AI 调用
                .defaultTools(new DateTimeTools(),new FileReaderTools())  // 一次性注册多个工具
                // 配置工具回调对象（toolCallback），用于处理工具调用的结果或中间逻辑（例如之前提到的天气工具回调）。
                // 作用：当 AI 模型调用工具（如DateTimeTools）时，框架通过回调机制获取工具返回的结果，并将结果传递给模型进行后续处理（如整理成自然语言回答）
                .defaultToolCallbacks(toolCallback)
                // chatMemory 负责 “存”：保存用户提问、AI 回复、时间戳等对话数据。
                // MessageChatMemoryAdvisor 负责 “取” 和 “用”：在需要时读取历史数据，传递给 AI 模型作为上下文
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem("你是一个聊天机器人，请以简洁准确的方式回答问题")
                .build();
    }
}
