package com.mita.chat.controller;

import com.mita.chat.dto.ConversationDto;
import com.mita.chat.dto.MessageDto;
import com.mita.chat.entity.Conversation;
import com.mita.chat.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PutMapping("/{username}")
    public Long createOrGetChat(@PathVariable String username){
        Conversation c = chatService.getOrCreatePrivateChat(username);
        return c.getId();
    }

    @GetMapping("/{id}/messages")
    public List<MessageDto> getMessages(@PathVariable Long id){
        return chatService.getMessagesDto(id);
    }

    @PostMapping("/{id}/messages")
    public MessageDto sendMessage(
            @PathVariable Long id,
            @RequestBody String content){
        return chatService.sendMessageDto(id, content);
    }

    @GetMapping
    public List<ConversationDto> getMyChats(){
        return chatService.getMyChats();
    }

}
