package tc;

import com.glmapper.ai.tc.ToolCallingApplication;
import com.glmapper.ai.tc.controller.UserControllerExecutor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName UserControllerExecutorTest
 * @Description TODO
 * @Author masen.27
 * @Date 2025/7/24 14:24
 * @Version 1.0
 */
@SpringBootTest(classes = ToolCallingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerExecutorTest {

    // 模拟OllamaChatModel依赖
    @Autowired
    private OllamaChatModel chatModel;

    // 注入被测试对象，并自动注入模拟的依赖
    @Autowired
    private UserControllerExecutor executor;

    @Test
    void testManualExecToolsWithoutToolCall() {

        String result = executor.manualExecTools("今天的日期是多少？");
        System.out.println(result);

    }




}
