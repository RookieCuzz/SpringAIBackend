package com.cuzz.springaidemo.config;

import com.cuzz.springaidemo.websocket.MyWebSocketClient;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.model.ApiKey;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

@Configuration
public class ChatModelConfig {
    public final ChatMemory chatMemory = new InMemoryChatMemory();
    private static final String DEEPSEEK_BASE_URL = "https://api.lkeap.cloud.tencent.com/v1";
    private static final String DEFAULT_DEEPSEEK_MODEL = "deepseek-v3";

    public static  String readFileContentFromClasspath() {
        ClassPathResource resource = new ClassPathResource("/prompt/system.txt");

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


    @Value("${centerWsURL}")
    private String url;

    @Bean
    public MyWebSocketClient webSocketClient() throws URISyntaxException {
        return new MyWebSocketClient(new URI(url));
    }


//    @Bean
//    public JedisPooled jedisPooled() {
//
//        HostAndPort hostAndPort = new HostAndPort("107.172.217.25", 6379);
//        // 创建 JedisClientConfig 对象并设置密码
//        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().password("yourpassword").connectionTimeoutMillis(
//                3000
//                )
//                .socketTimeoutMillis(120000)
//                .clientName("AI").build();
//
//
//        // 创建 JedisPooled 对象
//        return new JedisPooled(hostAndPort, clientConfig);
//    }
//    api-key: sk-tUYWHdgOWNcmWQkJsbluKbDgJTV3BuNNhskvZnPZwmJNEG0D
//    base-url: https://chatapi.littlewheat.com
    @Bean
    public EmbeddingModel embeddingModel() {
        OpenAiApi build = OpenAiApi.builder().apiKey(new SimpleApiKey("sk-tUYWHdgOWNcmWQkJsbluKbDgJTV3BuNNhskvZnPZwmJNEG0D")).baseUrl("https://chatapi.littlewheat.com")
                .build();

        return new OpenAiEmbeddingModel(build);
    }


    @Bean
    @Primary
    public VectorStore vectorStorePostgresql(JdbcTemplate jdbcTemplate,EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate,embeddingModel).build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(postgresDataSource());
    }
    @Bean
    public DataSource postgresDataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5432/testdb");
        ds.setUsername("postgres");
        ds.setPassword("cpddcpdd");
        return ds;
    }

//    public VectorStore vectorStoreRedis(JedisPooled jedisPooled,EmbeddingModel embeddingModel) {
//        return RedisVectorStore.builder(jedisPooled, embeddingModel)
//                .indexName("spring-ai-index")                // Optional: defaults to "spring-ai-index"
//                .prefix("ai_vector:")                  // Optional: defaults to "embedding:"
//                .metadataFields(                         // Optional: define metadata fields for filtering
//                        RedisVectorStore.MetadataField.tag("knowledge"))
//                .initializeSchema(true)                   // Optional: defaults to false
//                .batchingStrategy(new TokenCountBatchingStrategy()) // Optional: defaults to TokenCountBatchingStrategy
//                        .
//                build();
//    }


    @Bean
    public ChatClient chatClient(@Lazy OpenAiChatModel openAiChatModel){
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(new PromptChatMemoryAdvisor(chatMemory)) //,new QuestionAnswerAdvisor(vectorStoreRedis,queryConfig)
                .build();
    }

//    @Bean
//    public OpenAiApi chatCompletionApi() {
//        return new OpenAiApi(DEEPSEEK_BASE_URL, "sk-ODEEi8mL95HQdmN40uglPBpJ3axn9YbBZ3hVQMQTuVGhLEYZ");
//    }

//    @Bean
//    public OpenAiChatModel openAiChatModel(OpenAiApi openAiApi) {
//        return new OpenAiChatModel(openAiApi, OpenAiChatOptions.builder().model(DEFAULT_DEEPSEEK_MODEL).build());
//    }

}
