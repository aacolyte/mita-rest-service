package com.mita.chat.repository;

import com.mita.chat.dto.ConversationDto;
import com.mita.chat.entity.Conversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("""
    SELECT c
    FROM Conversation c
    JOIN ConversationParticipant  cp ON cp.conversation = c
    WHERE cp.user.id = :userId  
""")
    Page<Conversation> findUserConversations(Long userId, Pageable pageable);



    @Query("""
        SELECT new com.mita.chat.dto.ConversationDto(
            c.id,
            u.username,
            u.avatar,
            m.content,
            m.createdAt
        )
        FROM Conversation c
        JOIN ConversationParticipant cp2
            ON cp2.conversation = c
            AND cp2.user.id != :userId
        JOIN User u ON u.id = cp2.user.id
        LEFT JOIN Message m ON m.id = c.lastMessageId
        WHERE EXISTS (
            SELECT 1
            FROM ConversationParticipant cp1
            WHERE cp1.conversation = c
                AND cp1.user.id = :userId
        )  
        ORDER BY c.lastMessageTime DESC
""")
    Page<ConversationDto> findChatsWithLastMessage(
            Long userId, Pageable pageable
    );

    @Modifying
    @Query("""
    UPDATE Conversation c
    SET c.lastMessageId = :messageId,
        c.lastMessageTime = :createdAt
    WHERE c.id = :conversationId
""")
    void updateLastMessage(Long conversationId, Long messageId, Instant createdAt);
}
