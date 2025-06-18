package com.example.codingshuttle.ChatApp.services;

import com.example.codingshuttle.ChatApp.Model.ChatEntry;
import com.example.codingshuttle.ChatApp.Model.Message;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;


import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ChatService {
    private final List<Message> chatHistory = new ArrayList<>();
    private final Map<String,String> userState = new HashMap<>();
    private final Map<String, List<ChatEntry>> userChatLogs = new HashMap<>();

    public  Message processMessage(String sender, String messageText){
        System.out.println(messageText);
        String reply;
        String normalized = messageText.trim().toLowerCase();

        if (!userState.containsKey(sender)){
            reply = "Hello " + sender + "! Welcome to our chatbot. Type 'menu' to see options.";
            userState.put(sender,"greeted");
        }
        else {
            switch (normalized){
                case "menu":
                    reply = "Here are your options:\n1. info\n2. contact\n3. exit";
                    userState.put(sender,"menu_shown");
                    break;
                case "info":
                    reply = "This is a smart chatbot demo built using Spring Boot. ";
                    userState.put(sender,"info_given");
                    break;
                case "contact":
                    reply = "You can contact us at support@example.com";
                    userState.put(sender,"contact_given");
                    break;
                case "exit":
                    reply = "Goodbye, " + sender + "! Have a great day!";
                    userState.remove(sender);
                    break;
                default:
                    reply = "I'm sorry, I didn't understand that. Please type 'menu' to see options.";
            }
        }

//        switch (messageText.trim().toLowerCase()){
//            case "hi":
//                reply = "Hello " + sender + ", how can I help you today";
//                break;
//            case "hello":
//                reply = "Hello " + sender + ", how can I help you today";
//                break;
//            case "help":
//                reply = "Sure! I can assit you with your queries. Ask me anything.";
//                break;
//            default:
//                reply = "Sorry, I didn’t understand that. Please type 'help'.";
//        }

        Message message = new Message(sender, messageText,reply, new Date());
        //firebase code:
        saveToFirebase(message);


        ChatEntry entry = new ChatEntry(messageText,reply,new Date());
        userChatLogs.computeIfAbsent(sender,k -> new ArrayList<>()).add(entry);
        chatHistory.add(message);
        return message;
    }

    private void saveToFirebase(Message message) {
        try {
            Firestore db = FirestoreClient.getFirestore();
            db.collection("users")
                    .document(message.getSender())
                    .collection("messages")
                    .add(message);
        } catch (Exception e) {
            System.err.println("⚠️ Firebase save error: " + e.getMessage());
        }
    }


    public List<Message> getAllMessages() {
        return chatHistory;
    }

    public List<ChatEntry> getChatLogForUser(String sender) {
        return userChatLogs.getOrDefault(sender,new ArrayList<>());
    }

    public List<Message> getMessagesForUser(String sender) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        ApiFuture<QuerySnapshot> future = db.collection("users")
                .document(sender)
                .collection("messages")
                .get();

        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream()
                .map(doc -> doc.toObject(Message.class))
                .collect(Collectors.toList());
    }

}
