package jdbc;

import com.glmapper.ai.chat.memory.jdbc.JdbcMemoryApplication;
import com.glmapper.ai.chat.memory.jdbc.service.ChatMemoryService;
import org.junit.jupiter.api.*;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ChatMemoryService 集成测试
 * <p>
 * 测试聊天记忆功能，验证对话上下文的保持能力
 *
 * @author glmapper
 * @since 2025-01-20
 */
@SpringBootTest(classes = JdbcMemoryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcChatMemoryTest {

    @Autowired
    private ChatMemoryService chatMemoryService;

    @Autowired
    private JdbcChatMemoryRepository chatMemoryRepository;

    @BeforeEach
    void setUp() {
        chatMemoryRepository.findConversationIds().forEach(conversationId -> {
            if (conversationId.startsWith("test-")) {
                // 清理测试用的对话记录
                chatMemoryRepository.deleteByConversationId(conversationId);
            }
        });
        // 检查服务是否正确注入
        assertNotNull(chatMemoryService, "ChatMemoryService should be injected");
    }

    @Test
    @Order(1)
    @DisplayName("测试聊天记忆功能 - 上下文保持")
    void testChatMemoryContextRetention() {
        String CONVERSATION_ID = "test-naming-202505281800";
        // 第一轮对话：自我介绍
        String firstMessage = "hello, my name is glmapper";
        String firstResponse = chatMemoryService.call(firstMessage, CONVERSATION_ID);

        assertNotNull(firstResponse, "第一次响应不应该为空");
        System.out.println("第一轮对话:");
        System.out.println("用户: " + firstMessage);
        System.out.println("AI: " + firstResponse);

        // 第二轮对话：询问之前提到的信息
        String secondMessage = "do you remember my name? ";
        String secondResponse = chatMemoryService.call(secondMessage, CONVERSATION_ID);

        assertNotNull(secondResponse, "第二次响应不应该为空");
        System.out.println("\n第二轮对话:");
        System.out.println("用户: " + secondMessage);
        System.out.println("AI: " + secondResponse);

        // 验证AI是否记住了用户的名字
        assertTrue(secondResponse.contains("glmapper") || secondResponse.toLowerCase()
                .contains("glmapper"), "AI 应该记住用户的名字");

        chatMemoryRepository.deleteByConversationId(CONVERSATION_ID);
    }

    @Test
    @Order(2)
    @DisplayName("测试多轮对话的连续性")
    void testMultiTurnConversationContinuity() {
        String CONVERSATION_ID = "test-naming-202505291800";
        // 模拟一个完整的对话流程
        String[] messages = {"我正在学习 Spring AI框架", "Spring AI有哪些主要特性？", "刚才你提到的特性中，哪个最重要？"};
        String[] responses = new String[messages.length];
        for (int i = 0; i < messages.length; i++) {
            responses[i] = chatMemoryService.call(messages[i], CONVERSATION_ID);
            assertNotNull(responses[i], "第" + (i + 1) + "次响应不应该为空");
            System.out.println("\n第" + (i + 1) + "轮对话:");
            System.out.println("用户: " + messages[i]);
            System.out.println("AI: " + responses[i]);
        }
        // 验证最后一次响应是否引用了之前的对话内容
        String lastResponse = responses[responses.length - 1];
        assertTrue(lastResponse.length() > 10, "最后的响应应该有实质内容");
        chatMemoryRepository.deleteByConversationId(CONVERSATION_ID);
    }

    @Test
    @Order(3)
    @DisplayName("测试对话ID的一致性")
    void testConversationIdConsistency() {
        String CONVERSATION_ID = "test-naming-202505281801";
        // 这个测试主要验证使用相同的 CONVERSATION_ID
        // 在实际应用中，可以通过日志或其他方式验证
        String message1 = "请记住这个数字：12345";
        String message2 = "刚才我说的数字是什么？";

        String response1 = chatMemoryService.call(message1, CONVERSATION_ID);
        String response2 = chatMemoryService.call(message2, CONVERSATION_ID);

        assertNotNull(response1, "第一次响应不应该为空");
        assertNotNull(response2, "第二次响应不应该为空");

        System.out.println("对话ID一致性测试:");
        System.out.println("用户1: " + message1);
        System.out.println("AI1: " + response1);
        System.out.println("用户2: " + message2);
        System.out.println("AI2: " + response2);

        // 验证AI是否记住了数字
        assertTrue(response2.contains("12345"), "AI应该记住之前提到的数字");
        // chatMemoryRepository.deleteByConversationId(CONVERSATION_ID);
    }

    @Test
    @Order(4)
    @DisplayName("测试对话ID的非一致性")
    void testConversationIdNonConsistency() {
        String CONVERSATION_ID1 = "test-naming-202505281801";
        String CONVERSATION_ID2 = "test-naming-202505281802";
        // 这个测试主要验证使用相同的 CONVERSATION_ID
        // 在实际应用中，可以通过日志或其他方式验证
        String message1 = "请记住这个数字：12345";
        String message2 = "刚才我说的数字是什么？";

        String response1 = chatMemoryService.call(message1, CONVERSATION_ID1);
        String response2 = chatMemoryService.call(message2, CONVERSATION_ID2);

        assertNotNull(response1, "第一次响应不应该为空");
        assertNotNull(response2, "第二次响应不应该为空");

        System.out.println("对话ID一致性测试:");
        System.out.println("用户1: " + message1);
        System.out.println("AI1: " + response1);
        System.out.println("用户2: " + message2);
        System.out.println("AI2: " + response2);

        // 验证AI是否记住了数字
        assertFalse(response2.contains("12345"), "AI应该记住之前提到的数字");


        chatMemoryRepository.deleteByConversationId(CONVERSATION_ID1);
        chatMemoryRepository.deleteByConversationId(CONVERSATION_ID2);
    }
}