package com.dteema.dteema.dto.room;

import com.dteema.dteema.dto.user.UserDTO;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDTO {

    private Long id;
    private String name;
    private String description;
    private UserDTO createdBy;
    private Boolean isPrivate;
    private Integer memberCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}