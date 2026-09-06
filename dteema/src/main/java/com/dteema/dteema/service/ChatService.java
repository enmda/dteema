package com.dteema.dteema.service;

import com.dteema.dteema.dto.chat.ChatMessageResponse;
import com.dteema.dteema.dto.chat.CreateChatSessionResponse;
import com.dteema.dteema.exception.ChatSessionFullException;
import com.dteema.dteema.exception.ChatSessionNotFoundException;
import com.dteema.dteema.exception.ChatSessionUnauthorizedException;
import com.dteema.dteema.model.User;
import com.dteema.dteema.model.chatting.Message;
import com.dteema.dteema.model.chatting.Session;
import com.dteema.dteema.model.chatting.SessionParticipant;
import com.dteema.dteema.repository.MessageRepository;
import com.dteema.dteema.repository.SessionParticipantRepository;
import com.dteema.dteema.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final int MAX_PARTICIPANTS = 2;

    private final SessionRepository sessionRepository;
    private final SessionParticipantRepository sessionParticipantRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public CreateChatSessionResponse createSession(User creator) {
        Session session = sessionRepository.save(Session.builder()
                .createdBy(creator)
                .build());

        sessionParticipantRepository.save(SessionParticipant.builder()
                .session(session)
                .user(creator)
                .build());

        return CreateChatSessionResponse.builder()
                .sessionId(session.getId())
                .build();
    }

    @Transactional
    public void joinOrValidateParticipant(UUID sessionId, User user) {
        Session session = sessionRepository.findByIdForUpdate(sessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException("Chat session was not found"));

        if (sessionParticipantRepository.existsBySessionIdAndUserId(sessionId, user.getId())) {
            return;
        }

        long participantCount = sessionParticipantRepository.countBySessionId(sessionId);
        if (participantCount >= MAX_PARTICIPANTS) {
            throw new ChatSessionFullException("This chat key is already used");
        }

        sessionParticipantRepository.save(SessionParticipant.builder()
                .session(session)
                .user(user)
                .build());
    }

    @Transactional
    public ChatMessageResponse sendMessage(UUID sessionId, User sender, String content) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ChatSessionNotFoundException("Chat session was not found"));

        if (!sessionParticipantRepository.existsBySessionIdAndUserId(sessionId, sender.getId())) {
            throw new ChatSessionUnauthorizedException("Join the chat session before sending messages");
        }

        Message message = messageRepository.save(Message.builder()
                .session(session)
                .sender(sender)
                .content(content.trim())
                .build());

        return toResponse(message);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getMessages(UUID sessionId, User user) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new ChatSessionNotFoundException("Chat session was not found");
        }
        if (!sessionParticipantRepository.existsBySessionIdAndUserId(sessionId, user.getId())) {
            throw new ChatSessionUnauthorizedException("Only chat participants can read this session");
        }

        return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ChatMessageResponse toResponse(Message message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .sessionId(message.getSession().getId())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
