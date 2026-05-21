package com.mita.chat.dto;

import java.time.Instant;

public class ConversationDto {
    private Long id;
    private String username;
    private String avatar;
    private String lastMessage;
    private Instant lastMessageTime;

    public ConversationDto(Long id, String username, String avatar, String lastMessage, Instant lastMessageTime) {
        this.id = id;
        this.username = username;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public Instant getLastMessageTime() {
        return lastMessageTime;
    }
}
