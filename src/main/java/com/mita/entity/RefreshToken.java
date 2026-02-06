package com.mita.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    @Column(unique = true, nullable = false)
    private String token;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private Date expiryDate;

    public RefreshToken() {
    }

    public RefreshToken(Long id, String token, User user, Date expiryDate) {
        Id = id;
        this.token = token;
        this.user = user;

        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
