package com.mita.chat.repository;

import com.mita.chat.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
    SELECT c
    FROM Conversation c
    JOIN ConversationParticipant  cp ON cp.conversation = c
    WHERE cp.user.id = :userId  
""")
    List<Conversation> findUserConversations(Long userId);
}
