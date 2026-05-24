package com.mita.chat.service;

import com.mita.chat.dto.ConversationDto;
import com.mita.chat.dto.MessageDto;
import com.mita.chat.dto.MessagePageDto;
import com.mita.chat.entity.Conversation;
import com.mita.chat.entity.ConversationParticipant;
import com.mita.chat.entity.Message;
import com.mita.chat.repository.ConversationParticipantRepository;
import com.mita.chat.repository.ConversationRepository;
import com.mita.chat.repository.MessageRepository;
import com.mita.entity.User;
import com.mita.repository.UserRepository;
import com.mita.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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


//    @Transactional
//    public Message sendMessage(Long conversationId, String content) {
//        User sender = userService.getCurrentUser();
//
//        validateParticipant(conversationId, sender.getId());
//
//        Conversation conversation = conversationRepository.findById(conversationId)
//                .orElseThrow();
//
//        Message message = new Message(conversation, sender, content);
//
//        return messageRepository.save(message);
//    }


//    public List<Message> getMessages(Long conversationId) {
//        Long userId = userService.getCurrentUser().getId();
//
//        validateParticipant(conversationId, userId);
//
//        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
//    }

    @Transactional
    public MessageDto sendMessageDto(Long conversationId, String content) {
        User sender = userService.getCurrentUser();

        validateParticipant(conversationId, sender.getId());

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow();

        Message message = new Message(conversation, sender, content);
        messageRepository.save(message);


        conversationRepository.updateLastMessage(
                conversationId,
                message.getId(),
                message.getCreatedAt()
        );

        return mapToDto(message);
    }

    public Page<ConversationDto> getMyChats(Pageable pageable) {
        Long userId = userService.getCurrentUser().getId();

        int maxSize = 50;


        if (pageable.getPageSize() > maxSize) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    maxSize,
                    pageable.getSort()
            );
        }

        return conversationRepository.findChatsWithLastMessage(userId, pageable);
    }

    @Transactional
    public void deleteChat(Long conversationId) {
        Long userId = userService.getCurrentUser().getId();

        validateParticipant(conversationId, userId);

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow();

        messageRepository.deleteByConversationId(conversationId);
        participantRepository.deleteByConversationId(conversationId);
        conversationRepository.deleteById(conversationId);
    }




    private void validateParticipant(Long conversationId, Long userId) {
        boolean isParticipant = participantRepository
                .existsByConversationIdAndUserId(conversationId,userId);

        if(!isParticipant) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied: not a participant");
        }
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

    public MessagePageDto getMessagesDto(Long conversationId, Long beforeId) {
        Long userId = userService.getCurrentUser().getId();

        validateParticipant(conversationId, userId);

        List<Message> messages;

        if(beforeId == null){
            messages = messageRepository
                    .findTop40ByConversationIdOrderByIdDesc(conversationId);
        } else {
            messages = messageRepository
                    .findTop40ByConversationIdAndIdLessThanOrderByIdDesc(conversationId, beforeId);
        }

        boolean hasMore = messages.size() == 40;

        return new MessagePageDto(
                messages.stream().map(this::mapToDto).toList(),
                hasMore
        );
    }






}
