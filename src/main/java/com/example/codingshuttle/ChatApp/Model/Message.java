package com.example.codingshuttle.ChatApp.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message {
    private String sender;
    private String message;
    private String reply;
    private Date localDateTime;
}
