package com.dteema.dteema.model;

public enum RoomRole {
    ADMIN,      // Can delete room, kick members, change settings
    MODERATOR,  // Can delete messages, mute users in this room
    MEMBER      // Regular member, can send messages
}
