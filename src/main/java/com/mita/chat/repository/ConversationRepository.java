package com.mita.chat.repository;

import com.mita.chat.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
    SELECT c
    FROM Conversation c
    JOIN ConversationParticipant  cp ON cp.conversation = c
    WHERE cp.user.id = :userId  
""")
    Page<Conversation> findUserConversations(Long userId, Pageable pageable);
}
