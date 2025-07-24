package com.glmapper.ai.tc.tools.tool;

import org.springframework.ai.tool.annotation.Tool;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassName FileReaderTool
 * @Description 读取文件工具
 * @Author masen.27
 * @Date 2025/7/23 17:42
 * @Version 1.0
 */
public class FileReaderTools {

    @Tool(description = "阅读一个文件并将其内容打印出来")
    // 用于读取文件内容并打印，同时支持两种读取方式：直接读取文件系统中的文件和读取类路径（classpath）下的资源文件
    public String readFileAndPrint(String filePath) {
        String content = "";
        try{
            // 1. 第一阶段：尝试读取文件系统中的文件
            Path path = Paths.get(filePath);
            content = Files.readString(path);
            System.out.println("File content: " + content);
        } catch (IOException e) {
            // 2. 第二阶段：尝试读取类路径（classpath）下的资源文件
            // 使用 classloader 读取 classpath 下的文件
            try{
                // 通过类加载器获取类路径下的资源输入流
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
                if (inputStream != null) {
                    // 读取输入流的所有字节并转换成字符串
                    content = new String(inputStream.readAllBytes());
                    System.out.println("File content from classpath:\n" + content);
                }
            }catch (Exception ex){
                content = "Error reading file from classpath: " + ex.getMessage();
                System.out.println("Error reading file from classpath: " + ex.getMessage());
            }
            System.out.println("Error reading file: " + e.getMessage());
        }

        return content;
    }
}
