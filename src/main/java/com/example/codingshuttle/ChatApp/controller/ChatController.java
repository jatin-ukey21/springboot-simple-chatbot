package com.example.codingshuttle.ChatApp.controller;

import com.example.codingshuttle.ChatApp.Model.ChatEntry;
import com.example.codingshuttle.ChatApp.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.codingshuttle.ChatApp.services.ChatService;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PostMapping("/send1")
    public Message sendMessage(@RequestParam String sender,@RequestParam String message){
        return chatService.processMessage(sender,message);
    }

    @GetMapping("/messages")
    public List<Message> getMessages(){
        return chatService.getAllMessages();
    }

    @GetMapping("/test")
    public String testApi() {
        return "API is working!";
    }

    @GetMapping("/history")
    public List<Message> getChatHistory(@RequestParam String sender) throws ExecutionException, InterruptedException {
//        return  chatService.getChatLogForUser(sender);
        return  chatService.getMessagesForUser(sender);
    }
}
