package com.mita.chat.service;

import com.mita.chat.dto.ConversationDto;
import com.mita.chat.dto.MessageDto;
import com.mita.chat.entity.Conversation;
import com.mita.chat.entity.ConversationParticipant;
import com.mita.chat.entity.Message;
import com.mita.chat.repository.ConversationParticipantRepository;
import com.mita.chat.repository.ConversationRepository;
import com.mita.chat.repository.MessageRepository;
import com.mita.entity.User;
import com.mita.repository.UserRepository;
import com.mita.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {
    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public ChatService(ConversationRepository conversationRepository,
                       ConversationParticipantRepository participantRepository,
                       MessageRepository messageRepository,
                       UserRepository userRepository,
                       UserService userService) {

        this.conversationRepository = conversationRepository;
        this.participantRepository = participantRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @Transactional
    public Conversation getOrCreatePrivateChat(String username) {
        User current = userService.getCurrentUser();
        User target = userService.getUserByUsernamePrivate(username);

        if (current.getId().equals(target.getId())) {
            throw new IllegalArgumentException("Cannot chat with yourself");
        }

        List<ConversationParticipant> myChats = participantRepository.findByUserId(current.getId());

        for (ConversationParticipant cp : myChats) {
            Conversation c = cp.getConversation();

            boolean hasTarget = participantRepository.findByUserId(target.getId())
                    .stream()
                    .anyMatch(p -> p.getConversation().getId().equals(c.getId()));

            if (hasTarget) {
                return c;
            }
        }
        Conversation conversation = new Conversation();
        conversationRepository.save(conversation);

        participantRepository.save(new ConversationParticipant(conversation, current));
        participantRepository.save(new ConversationParticipant(conversation, target));

        return conversation;
    }


    @Transactional
    public Message sendMessage(Long conversationId, String content) {
        User sender = userService.getCurrentUser();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow();

        Message message = new Message(conversation, sender, content);

        return messageRepository.save(message);
    }


    public List<Message> getMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }

    @Transactional
    public MessageDto sendMessageDto(Long conversationId, String content) {
        User sender = userService.getCurrentUser();

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow();

        Message message = new Message(conversation, sender, content);

        messageRepository.save(message);

        return mapToDto(message);
    }

    public List<ConversationDto> getMyChats(){
        Long userId = userService.getCurrentUser().getId();

        List<Conversation> conversations = conversationRepository.findUserConversations(userId);

        return conversations.stream().map(c -> {
            Message lastMessage =  messageRepository
                    .findTopByConversation_IdOrderByCreatedAtDesc(c.getId());

            User otherUser = participantRepository
                    .findByConversationId(c.getId())
                    .stream()
                    .map(ConversationParticipant::getUser)
                    .filter(u -> !u.getId().equals(userId))
                    .findFirst()
                    .orElse(null);

            return new ConversationDto(
                    c.getId(),
                    otherUser != null ? otherUser.getUsernameField() : null,
                    otherUser != null ? otherUser.getAvatar() : null,
                    lastMessage != null ? lastMessage.getContent() : null,
                    lastMessage != null ? lastMessage.getCreatedAt() : null
            );
        }).toList();
    }


    private MessageDto mapToDto(Message m){
        return new MessageDto(
                m.getId(),
            m.getSender().getUsername(),
            m.getSender().getAvatar(),
            m.getContent(),
            m.getCreatedAt()
        );
    }

    public List<MessageDto> getMessagesDto(Long conversationId) {
        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }






}
