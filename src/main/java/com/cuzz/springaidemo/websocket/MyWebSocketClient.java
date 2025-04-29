package com.cuzz.springaidemo.websocket;



import com.cuzz.springaidemo.services.AiChatService;
import com.cuzz.springaidemo.services.RedisVectorService;
import com.cuzz.springaidemo.models.dto.RoleDTO;
import com.cuzz.springaidemo.services.DocumentService;
import com.cuzz.springaidemo.services.RoleService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

public class MyWebSocketClient extends WebSocketClient {
    @Resource
    private ChatClient chatClient;

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    RoleService roleService;

    @Resource
    DocumentService documentService;
    @Resource
    private RedisVectorService redisVectorService;

    @Value("${centerWsURL}")
    String url;

    @Resource
    private AiChatService aiChatService;

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }




    @Override
    public void onMessage(String message) {
        // 使用新线程处理消息
        new Thread(() -> {
            Gson gson = new Gson();

            // 解析 JSON 字符串
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

            // 提取 userId、groupId、role 和 content
            String sender = jsonObject.get("sender").getAsString();
            String roleName = jsonObject.get("reciver").getAsString(); // 角色
            String content = jsonObject.get("content").getAsString(); // 消息内容

            String response = aiChatService.callWithRoleKnowledge(roleName, content, sender + roleName);

            // 打印角色
            System.out.println("角色为" + roleName);


            // 更新 content 为响应内容
            jsonObject.remove("content");
            jsonObject.remove("sender");
            jsonObject.remove("reciver");
            jsonObject.addProperty("reciver", sender);
            jsonObject.addProperty("sender", roleName);
            jsonObject.addProperty("content", response);

            // 设置消息类型为 aiResponse
            jsonObject.addProperty("type", "aiResponse");

            // 打印最终的 JSON 响应
            System.out.println(jsonObject);

            // 发送响应
            this.send(jsonObject.toString());

        }).start();

        System.out.println("📩 收到消息: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("❌ WebSocket 连接关闭，原因：" + reason);

        // 3 秒后尝试重连
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("🔄 尝试重新连接...");
                this.reconnect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }


    @Override
    public void onError(Exception ex) {
        System.out.println("⚠️ WebSocket 发生错误: " + ex.getMessage());
    }
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("🔗 WebSocket 连接已建立");

        // 启动心跳定时器，定期发送心跳消息
        Timer heartbeatTimer = new Timer(true);
        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isOpen()) {
                    sendHeartbeat();  // 发送心跳消息
                }
            }
        }, 0, 30000);  // 每 5 秒发送一次心跳
    }

    private void sendHeartbeat() {
        // 发送心跳消息，可以自定义心跳消息的内容
//        String heartbeatMessage = "{\"type\":\"heartbeat\"}";
        sendPing();
//        System.out.println("💓 发送心跳消息");
    }



}
