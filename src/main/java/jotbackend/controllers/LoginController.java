package jotbackend.controllers;

import com.google.api.client.json.jackson2.JacksonFactory;
import jotbackend.classes.AuthenticationResponse;
import jotbackend.classes.JotUserDetails;
import jotbackend.classes.User;
import jotbackend.services.ContactService;
import jotbackend.services.UserService;
import jotbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
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
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    // @PostMapping(path = "/login", produces = "application/json")
    @PostMapping(path = "/login")
    ResponseEntity<?> authenticate (@RequestBody Map<String, String> request) throws Exception {

        String idTokenString = request.get("idTokenString");
        String accessToken = request.get("accessToken");

        NetHttpTransport transport = new NetHttpTransport();
        JacksonFactory jsonFactory = new JacksonFactory();
        String CLIENT_ID = "924098505527-ta2u3pvgjksj497p9lu7rcahkvfoq1vs.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        GoogleIdToken idToken = verifier.verify(idTokenString);
        User user = null;

        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Check if existing user
            String gid = payload.getSubject();
            System.out.println("User Google ID: " + gid);
            Optional<User> optionalUser = userService.getUserByGid(gid);

            if (optionalUser.isPresent()) {
                System.out.println("User already exists");
                user = optionalUser.get();
            }
            else {
                System.out.println("Creating new user");
                // Get profile information from payload
                String email = payload.getEmail();
                // String pictureUrl = (String) payload.get("picture");
                String familyName = (String) payload.get("family_name");
                String givenName = (String) payload.get("given_name");

                user = userService.addNewUser(gid, givenName, familyName, email, "");
                // Integer userId = 15;
                System.out.println(contactService.getAndAddUserContacts(user.getId(), accessToken));
            }

        } else {
            System.out.println("Invalid ID token.");
        }

        /*
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String userPassword = passwordEncoder.encode(" ");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailAddress(), userPassword));
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username password combination", e);
        }
         */

        JotUserDetails userDetails = new JotUserDetails(user);
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}
