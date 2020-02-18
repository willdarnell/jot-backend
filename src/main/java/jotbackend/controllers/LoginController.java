package jotbackend.controllers;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import jotbackend.classes.User;
import jotbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@Controller
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    // @Resource
    // public Environment env;

    // private String clientId = env.getProperty("CLIENT_ID");
    // private String clientSecret = env.getProperty("CLIENT_SECRET");

    // @PostMapping(path = "/login", produces = "application/json")
    @PostMapping(path = "/login")
    String verifyToken (@RequestBody Map<String, String> request) throws GeneralSecurityException, IOException {
        String idTokenString = request.get("idTokenString");
        String accessToken = request.get("accessToken");


        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String CLIENT_ID = "924098505527-ta2u3pvgjksj497p9lu7rcahkvfoq1vs.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

// (Receive idTokenString by HTTPS POST)

        GoogleIdToken idToken = verifier.verify(idTokenString);
        TokenResponse tr = new TokenResponse();
        tr.setAccessToken(accessToken);

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            // String name = (String) payload.get("name");
            // String pictureUrl = (String) payload.get("picture");
            // String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            Optional<User> user = userService.getUserByGid(userId);
            if (user.isPresent()) {
                System.out.println("User already exists");
            }
            else {
                System.out.println(userService.addNewUser(userId, givenName, familyName, email, ""));
                /*
                GoogleCredential credential = new GoogleCredential.Builder()
                        .setTransport(transport)
                        .setJsonFactory(jsonFactory)
                        .setClientSecrets(clientId, clientSecret)
                        .build()
                        .setFromTokenResponse(tr);

                PeopleService people =
                        new PeopleService.Builder(transport, jsonFactory, credential).build();
                // PeopleService.Connections.List request = people.connections().list()
                */
            }

        } else {
            System.out.println("Invalid ID token.");
        }

        return idTokenString;
    }
}
