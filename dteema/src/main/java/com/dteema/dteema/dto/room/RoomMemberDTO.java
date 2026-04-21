package com.dteema.dteema.dto.room;

import com.dteema.dteema.dto.user.UserDTO;
import com.dteema.dteema.model.RoomRole;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMemberDTO {

    private Long id;
    private UserDTO user;
    private RoomRole role;
    private LocalDateTime joinedAt;
}