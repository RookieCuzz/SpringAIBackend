package com.cuzz.springaidemo.websocket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class WebSocketRunner implements CommandLineRunner {

    private final MyWebSocketClient webSocketClient;

    public WebSocketRunner(MyWebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }

    @Override
    public void run(String... args) {
        System.out.println("🚀 启动 WebSocket 客户端...");
        webSocketClient.connect();
    }
}
