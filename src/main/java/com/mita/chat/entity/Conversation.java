package com.mita.chat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Conversation() {
    }

    public Long getId() {
        return id;
    }
}
