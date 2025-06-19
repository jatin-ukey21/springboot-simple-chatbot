package com.example.codingshuttle.ChatApp.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
public class FirebaseInitializer {

    @PostConstruct
    public void init() {
        try {
            /*
            InputStream serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream("firebase-service-account.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

            System.out.println("Firebase initialized.");

             */

            //FOR FIREBASE RENDER
            String firebaseJson = System.getenv("FIREBASE_CONFIG");

            if (firebaseJson == null || firebaseJson.isEmpty()) {
                throw new IllegalStateException("FIREBASE_CONFIG env variable is missing!");
            }

            // Write the JSON to a temporary file
            Path tempFile = Files.createTempFile("firebase", ".json");
            Files.write(tempFile, firebaseJson.getBytes(StandardCharsets.UTF_8));

            FileInputStream serviceAccount = new FileInputStream(tempFile.toFile());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized from ENV");
            }
        } catch (Exception e) {
            System.err.println("Firebase init error: " + e.getMessage());
        }
    }
}

