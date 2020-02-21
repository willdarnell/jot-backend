package jotbackend.controllers;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.people.v1.PeopleService;
import jotbackend.classes.User;
import jotbackend.services.ContactService;
import jotbackend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@Controller
@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private ContactService contactService;

    // @PostMapping(path = "/login", produces = "application/json")
    @PostMapping(path = "/login")
    String verifyToken (@RequestBody Map<String, String> request) throws GeneralSecurityException, IOException {
        String idTokenString = request.get("idTokenString");
        String accessToken = request.get("accessToken");

        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String CLIENT_ID = "924098505527-ta2u3pvgjksj497p9lu7rcahkvfoq1vs.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Check if existing user
            String gid = payload.getSubject();
            System.out.println("User ID: " + gid);
            Optional<User> user = userService.getUserByGid(gid);

            if (user.isPresent()) {
                System.out.println("User already exists");
            }
            else {
                // Get profile information from payload
                String email = payload.getEmail();
                // String pictureUrl = (String) payload.get("picture");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                Integer userId = userService.addNewUser(gid, givenName, familyName, email, "");
                System.out.println(contactService.addUserContacts(userId, accessToken));
            }

        } else {
            System.out.println("Invalid ID token.");
        }

        return idTokenString;
    }
}
