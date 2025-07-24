package tc;

import com.glmapper.ai.tc.ToolCallingApplication;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Classname ToolCallingTests
 * @Description tool calling 测试
 * @Date 2025/5/29 15:40
 * @Created by glmapper
 */
@SpringBootTest(classes = ToolCallingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToolCallingTests {

    @Autowired
    private ChatClient chatClient; // 已经声明了Bean形式，并且配备了4个默认的函数

    @Test
    public void testToolCalling() {
        String answer = this.chatClient.prompt()
                // .user("今天是几号")
                .user("今天北京天气怎么样")
                .call()
                .content();
        System.out.println("AI回答: " + answer);
    }

    @Test
    public void testToolCallingWithParams() {
        String answer = this.chatClient.prompt()
                .user("帮我读取 spring-ai-tool-calling 模块下 application.yml 文件的内容, 文件路径为：/Users/masen.27/IdeaProjects/spring-ai-learn/spring-ai-tool-calling/src/main/resources/application.yml")
                .call()
                .content();
        System.out.println("AI回答: " + answer);
    }
}
