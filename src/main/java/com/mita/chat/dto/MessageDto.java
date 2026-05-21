package com.mita.chat.dto;

import java.time.Instant;

public class MessageDto {
    private Long id;
    private String senderUsername;
    private String senderAvatar;

    private String content;
    private Instant createdAt;

    public MessageDto(Long id, String senderUsername, String senderAvatar, String content, Instant createdAt) {
        this.id = id;
        this.senderUsername = senderUsername;
        this.senderAvatar = senderAvatar;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
