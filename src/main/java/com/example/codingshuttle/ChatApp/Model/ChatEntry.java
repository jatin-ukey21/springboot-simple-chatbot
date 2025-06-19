package com.example.codingshuttle.ChatApp.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatEntry {
    private String senderMessage;
    private String botReply;
    private Date timestamp;
}
