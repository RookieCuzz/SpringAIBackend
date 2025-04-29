package com.cuzz.springaidemo.controllers;

import com.cuzz.springaidemo.models.dto.ChatRequestDTO;
import com.cuzz.springaidemo.services.AiChatService;
import com.cuzz.springaidemo.services.RedisVectorService;
import com.cuzz.springaidemo.Test;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


@RestController
public class ChatAiController
{


    @Resource
    private ChatClient chatClient;

    @Resource
    private OpenAiChatModel openAiChatModel;


    @Resource
    private RedisVectorService aiservice;


    @Resource
    private AiChatService aiChatService;
    @PostMapping()


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

        String response = chatClient.prompt().user(message).call().content();
        return response;

    }

    @GetMapping("/api/openAiChatWithRole")
    @CrossOrigin
    public String openAiChatWithRole(
            @RequestBody ChatRequestDTO request

    ){
        String response = aiChatService.callWithRole(
                request.getKnowledgeName(),
                request.getRoleName(),
                request.getMessage(),
                request.getUuid());

        return response;

    }
    @GetMapping("/api/openAiChatWithRoleWithKnowledge")
    @CrossOrigin
    public String openAiChatWithRoleWithKnowledge(
            @RequestBody ChatRequestDTO request

    ){
        String response = aiChatService.callWithRoleKnowledge(
                request.getRoleName(),
                request.getMessage(),
                request.getUuid());

        return response;

    }
    @RequestMapping(value = "/api/openAiChatWithRole", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin
    public Flux<String> openAiChatWithRoleWithKnowledgeStream(@RequestBody ChatRequestDTO request) {
        // 获取原始的响应流
        Flux<String> response = aiChatService.callWithRoleKnowledgeStream(request.getRoleName(), request.getMessage(), request.getUuid());

        return Flux.create(sink -> {
            // 用于存储临时的一行数据
            StringBuilder currentLine = new StringBuilder();

            // 订阅响应流
            response.doOnNext(content -> {
                // 逐个字符处理内容
                for (char c : content.toCharArray()) {
                    if (c == '\n') {
                        // 遇到换行符，说明一行结束了，发送这一行数据
                        sink.next(currentLine.toString());
                        currentLine.setLength(0); // 清空当前行缓存
                    } else {
                        // 不是换行符，将字符加入到当前行
                        currentLine.append(c);
                    }
                }
            }).doFinally(signalType -> {
                // 如果最后一行没有换行符结束，发送剩余的数据
                if (currentLine.length() > 0) {
                    sink.next(currentLine.toString());
                }
                sink.complete(); // 完成流的传输
            }).subscribe();
        });
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
