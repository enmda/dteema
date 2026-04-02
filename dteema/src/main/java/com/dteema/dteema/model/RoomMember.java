package com.dteema.dteema.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
    name = "room_members",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_room_members_room_user",
        columnNames = {"room_id", "user_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Room-specific role
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private RoomRole role = RoomRole.MEMBER;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    // ===========================
    // Helper Methods
    // ===========================

    public boolean isAdmin() {
        return role == RoomRole.ADMIN;
    }

    public boolean isModerator() {
        return role == RoomRole.MODERATOR;
    }

    public boolean canManageRoom() {
        return role == RoomRole.ADMIN;
    }

    public boolean canModerateMessages() {
        return role == RoomRole.ADMIN || role == RoomRole.MODERATOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoomMember)) return false;
        RoomMember that = (RoomMember) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}