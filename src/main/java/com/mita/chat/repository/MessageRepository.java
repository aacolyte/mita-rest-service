package com.mita.chat.repository;

import com.mita.chat.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);

    Message findTopByConversation_IdOrderByCreatedAtDesc(Long conversationId);

    List<Message> findTop40ByConversationIdOrderByIdDesc(Long conversationId);

    List<Message> findTop40ByConversationIdAndIdLessThanOrderByIdDesc(
            Long conversationId, Long beforeId
    );


}
