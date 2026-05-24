package com.mita.chat.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Instant lastMessageTime;

    @Column
    private Long lastMessageId;

    public Conversation() {
    }

    public Conversation(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public Long getId() {
        return id;
    }

    public Instant getLastMessageTime() {
        return lastMessageTime;
    }

    public Long getLastMessageId() {
        return lastMessageId;

    }

    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public void setLastMessageTime(Instant lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
