package com.dteema.dteema.dto.common;

import com.dteema.dteema.dto.message.MessageDTO;
import com.dteema.dteema.dto.room.RoomDTO;
import com.dteema.dteema.dto.room.RoomMemberDTO;
import com.dteema.dteema.dto.user.UserDTO;
import com.dteema.dteema.model.Message;
import com.dteema.dteema.model.Room;
import com.dteema.dteema.model.RoomMember;
import com.dteema.dteema.model.User;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    // ===========================
    // User Mappings
    // ===========================

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    // ===========================
    // Room Mappings
    // ===========================

    public RoomDTO toRoomDTO(Room room) {
        if (room == null) {
            return null;
        }

        return RoomDTO.builder()
                .id(room.getId())
                .name(room.getName())
                .description(room.getDescription())
                .createdBy(toUserDTO(room.getCreatedBy()))
                .isPrivate(room.getIsPrivate())
                .memberCount(room.getMembers() != null ? room.getMembers().size() : 0)
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }

    // ===========================
    // RoomMember Mappings
    // ===========================

    public RoomMemberDTO toRoomMemberDTO(RoomMember roomMember) {
        if (roomMember == null) {
            return null;
        }

        return RoomMemberDTO.builder()
                .id(roomMember.getId())
                .user(toUserDTO(roomMember.getUser()))
                .role(roomMember.getRole())
                .joinedAt(roomMember.getJoinedAt())
                .build();
    }

    // ===========================
    // Message Mappings
    // ===========================

    public MessageDTO toMessageDTO(Message message) {
        if (message == null) {
            return null;
        }

        return MessageDTO.builder()
                .id(message.getId())
                .roomId(message.getRoom().getId())
                .user(toUserDTO(message.getUser()))
                .content(message.getContent())
                .isEdited(message.getIsEdited())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }
}