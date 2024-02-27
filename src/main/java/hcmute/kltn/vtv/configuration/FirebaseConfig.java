package hcmute.kltn.vtv.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Spring configuration class for Firebase initialization.
 */
@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase-configuration-file}")
    private String firebaseConfigPath;

    /**
     * Initializes the Firebase app if it doesn't already exist.
     */
    @PostConstruct
    public void initializeFirebaseApp() {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ClassPathResource(firebaseConfigPath).getInputStream()))
                        .build();
                FirebaseApp.initializeApp(options);
                logger.info("Firebase app has been initialized.");
            } catch (IOException e) {
                logger.error("Failed to initialize Firebase app:", e);
            }
        }
    }

    /**
     * Provides a Spring bean for injecting the FirebaseMessaging instance.
     */
    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }
}

