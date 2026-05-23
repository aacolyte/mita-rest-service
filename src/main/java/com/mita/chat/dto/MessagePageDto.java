package com.mita.chat.dto;

import java.util.List;

public class MessagePageDto {
    private List<MessageDto> messages;
    private boolean hasMore;

    public MessagePageDto(List<MessageDto> messages, boolean hasMore) {
        this.messages = messages;
        this.hasMore = hasMore;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public boolean isHasMore() {
        return hasMore;
    }
}
