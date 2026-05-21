package com.mita.chat.controller;

import com.mita.chat.dto.MessageDto;
import com.mita.chat.dto.SendMessageRequest;
import com.mita.chat.service.ChatService;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("chat.sendMessage")
    public void sendMessage(SendMessageRequest request){
        MessageDto message = chatService.sendMessageDto(
                request.getConversationId(),
                request.getContent()
        );
        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.getConversationId(),
                message);
    }
}
