package com.cuzz.springaidemo;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@RestController
public class ChatAiController
{


    @Resource
    private ChatClient chatClient;

    @Resource
    private OpenAiChatModel openAiChatModel;


    @Resource
    private Aiservice aiservice;

    @GetMapping("/api/openAiChat")
    public String openAiChat(@RequestParam(value = "message",defaultValue = "minecraft 信标的合成配方是什么？")String message){
        String content = chatClient.prompt().user(message).call().content();
        return content;

    }
    @GetMapping("/api/openAiChatF")
    public String openAiChatF(@RequestParam(value = "message",defaultValue = "minecraft 信标的合成配方是什么？")String message){
//        OpenAiChatOptions getLocation = OpenAiChatOptions.builder().function("GetLocation")
//                .build();
//        Test test = new Test();
//        String content = chatClient.prompt().user(message).functions(FunctionCallback.builder()
//                .method("getWeather",String.class).description("获取当地的天气")
//                .targetObject(test)
//                .build()).call().content();
//        return content;
        String s = readFileContentFromClasspath();
        System.out.println(s);
        return s;
    }
    @GetMapping("/api/openAiChatC")
    public String openAiChatC(@RequestParam(value = "message",defaultValue = "minecraft 信标的合成配方是什么？")String message){

        Test test = new Test();
        String content = chatClient.prompt().user(message).tools(FunctionCallback.builder()
                .method("getWeather",String.class).description("获取当地的天气")
                .targetObject(test)
                .build()).call().content();
        return content;

    }

    @GetMapping("/api/openAiChatX")
    public String openAiChatX(@RequestParam(value = "message",defaultValue = "minecraft 信标的合成配方是什么？")String message){

        String content = aiservice.getQueryDocs(message).get(0).getFormattedContent().toString();
        String response = chatClient.prompt().user(message).system(content).system(ChatConfig.readFileContentFromClasspath()).call().content();
        return response;

    }

    public String readFileContentFromClasspath() {
        ClassPathResource resource = new ClassPathResource("/prompt/data.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            return contentBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file from classpath: " , e);
        }
    }

}
