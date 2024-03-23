package com.eight.sailingship.dto.chat;

import lombok.*;

@Getter
@Setter
public class ChatMessage {
    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
}
