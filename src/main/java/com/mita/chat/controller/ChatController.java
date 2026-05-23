package com.mita.chat.controller;

import com.mita.chat.dto.ConversationDto;
import com.mita.chat.dto.MessageDto;
import com.mita.chat.dto.MessagePageDto;
import com.mita.chat.entity.Conversation;
import com.mita.chat.service.ChatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/{conversationId}/messages")
    public MessagePageDto getMessages(
            @PathVariable Long conversationId,
            @RequestParam(required = false) Long beforeId
    ){
        return chatService.getMessagesDto(conversationId, beforeId);
    }

    @PostMapping("/{id}/messages")
    public MessageDto sendMessage(
            @PathVariable Long id,
            @RequestBody String content){
        return chatService.sendMessageDto(id, content);
    }

    @GetMapping
    public Page<ConversationDto> getMyChats(
            @PageableDefault(size = 30, sort = "lastMessageTime", direction = Sort.Direction.DESC)
            Pageable pageable)
    {
        return chatService.getMyChats(pageable);
    }

}
