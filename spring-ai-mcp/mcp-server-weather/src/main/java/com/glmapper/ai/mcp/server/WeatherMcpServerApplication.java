package com.glmapper.ai.mcp.server;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName WeatherMcpServerApplication
 * @Description TODO
 * @Author masen.27
 * @Date 2025/7/25 18:54
 * @Version 1.0
 */
@SpringBootApplication
public class WeatherMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherMcpServerApplication.class, args);
    }

    // Spring MVC 的核心组件，负责管理所有 HTTP 接口端点的映射关系（即 URL 路径、请求方法与控制器方法的对应关系）
    // 例如，@GetMapping("/api/weather") 注解的方法会被它记录
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    // 该注解标记的方法会在当前 Bean 初始化完成后自动执行（即在Spring 容器加载完该类并注入依赖后）。这里用于在应用启动后打印接口信息
    @PostConstruct
    public void showEndpoints(){
        // 收集所有接口映射关系
        Map<String,Object> data = requestMappingHandlerMapping.getHandlerMethods().entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(), // 键：接口的请求信息（路径、方法等）
                        e -> e.getValue().getBean() // 值：处理该接口的控制器实例
                ));

        // 遍历输出所有接口信息
        data.entrySet().stream()
                .forEach(entry -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key+":"+value);
                });
    }
}
