package com.cuzz.springaidemo.services;


import com.cuzz.springaidemo.models.dto.RoleDTO;
import com.cuzz.springaidemo.models.dto.RoleKnowledgeDTO;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class AiChatService {

    @Resource
    ChatClient chatClient;

    @Resource
    VectorStore vectorStoreRedis;


    @Resource
    RoleService roleService;
    public RetrievalAugmentationAdvisor buildRetrievalAugmentationAdvisor(Set<String> knowledgeNames) {
        FilterExpressionBuilder expressionBuilder = new FilterExpressionBuilder();

        if (knowledgeNames.isEmpty()) {
        // 创建 DocumentRetriever 实例，使用向量存储进行检索
            DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                    .vectorStore(vectorStoreRedis) // 替换为您的 VectorStore 实例
                    .topK(1)
                    .similarityThreshold(0.99)
                    .build();

    // 构建 RetrievalAugmentationAdvisor
            return  RetrievalAugmentationAdvisor.builder()
                    .documentRetriever(documentRetriever)
                    // 根据需要添加其他组件，如 queryTransformers、queryExpander 等
                    .build();
        }

        Filter.Expression filterExpression = expressionBuilder.in("knowledge", knowledgeNames.toArray()).build();
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStoreRedis) // 替换为您的 VectorStore 实例
                .topK(1)
                .filterExpression(filterExpression)
                .similarityThreshold(0.99)
                .build();
        return  RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                // 根据需要添加其他组件，如 queryTransformers、queryExpander 等
                .build();
    }
    public RetrievalAugmentationAdvisor buildQuestionAnswerAdvisor(Set<String> knowledgeNames){
        // 使用 FilterExpressionBuilder 构建过滤表达式
        FilterExpressionBuilder expressionBuilder = new FilterExpressionBuilder();
        knowledgeNames.forEach(name->{
            System.out.println("知识库包含"+name);
        });

        if (knowledgeNames.isEmpty()){
            return null;
        }
        Filter.Expression filterExpression = expressionBuilder.in("knowledge", knowledgeNames.toArray()).build();
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStoreRedis) // 替换为您的 VectorStore 实例
                .topK(20)
                .filterExpression(filterExpression)
                .similarityThreshold(0.5)
                .build();
        return  RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                // 根据需要添加其他组件，如 queryTransformers、queryExpander 等
                .build();
    }
    public RetrievalAugmentationAdvisor buildQuestionAnswerAdvisor(String knowledgeName){
        // 使用 FilterExpressionBuilder 构建过滤表达式
        FilterExpressionBuilder expressionBuilder = new FilterExpressionBuilder();
        Filter.Expression filterExpression = expressionBuilder.in("knowledge", knowledgeName).build();
        DocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStoreRedis) // 替换为您的 VectorStore 实例
                .topK(1)
                .filterExpression(filterExpression)
                .similarityThreshold(0.99)
                .build();
        return  RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                // 根据需要添加其他组件，如 queryTransformers、queryExpander 等
                .build();
    }


    public String callWithRoleKnowledge(String roleName,String question,String uuid){

        if (roleName.equalsIgnoreCase("无")){
            String response = chatClient.prompt()
                    .user(question)
                    .call()
                    .content();
            return response;
        }

        if (roleName.equalsIgnoreCase("gork3")){

            RoleDTO grok = roleService.getRoleDTO(roleName);
            String response = chatClient.prompt()
                    .user(question)
                    .system(grok.getDescription())
                    .call()
                    .content();
            return response;
        }
        System.out.println("角色为" + roleName);

        // 从 roleService 获取角色信息
        RoleDTO roleDTO = roleService.getRoleDTO(roleName);

        String roleSystem = "你叫";

        // 如果角色存在，拼接角色信息
        if (roleDTO != null) {
//                System.out.println("人设信息为" + roleDTO.getDescription());
            roleSystem = roleSystem + roleDTO.getRoleName() + ",请不要不使用markdown格式,回答不要带有空格和空行.不要说废话 以下是你的人物设定" + roleDTO.getDescription();
        }else {
            roleSystem="啥也不知道";
        }
        Set<String> collect = roleService.getAllKnowledgeFromRole(roleName).stream().map(RoleKnowledgeDTO::getKnowledgeName).collect(Collectors.toSet());
        RetrievalAugmentationAdvisor questionAnswerAdvisor = buildQuestionAnswerAdvisor(collect);
        String response = chatClient.prompt()
                .system(roleSystem)
                .advisors(
                        a -> {
                            if (questionAnswerAdvisor!=null){
                                a.advisors(questionAnswerAdvisor);
                            }
                            a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, uuid)
                                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                            ;
                        })
                .user(question)
                .call()
                .content();
        System.out.println(response);
//        if (response.contains())
        String[] split = response.split("</think>");
        if (split.length>2){
            return split[1].trim();
        }else {
            return response;
        }

    }
    /**
     *
     * @param knowledgeName 知识文档名字
     * @param roleName  角色名字
     * @param question  对应问题
     * @param uuid  对话id
     * @return
     */

    public String callWithRole(String knowledgeName,String roleName,String question,String uuid){


        // 打印角色
        System.out.println("角色为" + roleName);

        // 从 roleService 获取角色信息
        RoleDTO roleDTO = roleService.getRoleDTO(roleName);

        String roleSystem = "你叫";

        // 如果角色存在，拼接角色信息
        if (roleDTO != null) {
//                System.out.println("人设信息为" + roleDTO.getDescription());
            roleSystem = roleSystem + roleDTO.getRoleName() + ",对了你所有的回复都是纯文本,字数控制在"+200+",只有一种格式!不使用markdown格式,不带有空格和空行. " + roleDTO.getDescription();
        }else {
            roleSystem="啥也不知道";
        }
        System.out.println("知识库名字是:"+knowledgeName);
        // 调用 chatClient 获取响应
        String response = chatClient.prompt()
                .system(roleSystem)
                .advisors(
                        a -> {
                            a.advisors(buildQuestionAnswerAdvisor(knowledgeName));
                            a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, uuid)
                                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                            ;
                        })
                .user(question)
                .call()
                .content();
        return response;

    }



    public Flux<String> callWithRoleKnowledgeStream(String roleName, String question, String uuid) {

        if (roleName.equalsIgnoreCase("无")){
            Flux<String> response = chatClient.prompt()
                    .user(question).stream().content();
            return response;
        }

        if (roleName.equalsIgnoreCase("gork3")){

            RoleDTO grok = roleService.getRoleDTO(roleName);
            Flux<String> response = chatClient.prompt()
                    .user(question)
                    .system(grok.getDescription()).stream().content();
            return response;
        }
        System.out.println("角色为" + roleName);

        // 从 roleService 获取角色信息
        RoleDTO roleDTO = roleService.getRoleDTO(roleName);

        String roleSystem = "你叫";

        // 如果角色存在，拼接角色信息
        if (roleDTO != null) {
//                System.out.println("人设信息为" + roleDTO.getDescription());
            roleSystem = roleSystem + roleDTO.getRoleName() + "以下是你的人物设定" + roleDTO.getDescription();
        }else {
            roleSystem="啥也不知道";
        }
        Set<String> collect = roleService.getAllKnowledgeFromRole(roleName).stream().map(RoleKnowledgeDTO::getKnowledgeName).collect(Collectors.toSet());

        RetrievalAugmentationAdvisor questionAnswerAdvisor = buildQuestionAnswerAdvisor(collect);
        Flux<String> response = chatClient.prompt()
                .system(roleSystem)
                .advisors(
                        a -> {
                            if (questionAnswerAdvisor!=null){
                                a.advisors(questionAnswerAdvisor);
                            }
                            a.param(CHAT_MEMORY_CONVERSATION_ID_KEY, uuid)
                                    .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                            ;
                        })
                .user(question)
                        .stream().content();
        System.out.println(response);
        return response;

    }

}
