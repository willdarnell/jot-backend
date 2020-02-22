package jotbackend.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jotbackend.classes.Contact;
import jotbackend.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public String getAndAddUserContacts(Integer userId, String accessToken) {
        String uri_base = "https://people.googleapis.com/v1/people/me/connections?personFields=names,emailAddresses,organizations,phoneNumbers&pageToken=";
        String nextPageToken = null;
        JsonNode nextPageNode = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        do {
                String uri = uri_base;
                if (nextPageToken != null) {
                    uri += nextPageToken;
                }
                try {
                    ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
                    if (result.getBody() != null) {
                        JsonNode jn = mapper.readValue(result.getBody(), JsonNode.class);
                        saveUserContacts(jn.get("connections"), userId);
                        // System.out.println(result.getBody());
                        nextPageNode = jn.get("nextPageToken");
                        if (nextPageNode != null) {
                            nextPageToken = nextPageNode.asText();
                        }
                        System.out.println(nextPageToken);
                    }
                } catch (HttpStatusCodeException e) {
                    System.out.println(e);
                } catch (JsonProcessingException e) {
                    System.out.println(e);
            }
        } while (nextPageNode != null);

        return "Success";
    }

    public void saveUserContacts(JsonNode contacts, Integer userId) {

        if (contacts.isArray()) {
            for (JsonNode contact : contacts) {

                String googleId = "", firstName = "", lastName = "", emailAddress = "", phoneNumber = "", organization = "", role = "";

                googleId = contact.get("resourceName").asText();

                // get names array
                JsonNode namesNode = contact.get("names");
                if (namesNode.isArray()) {
                    for (JsonNode name : namesNode) {
                        if (name.get("metadata").get("primary").asText() == "true") {
                            if (name.get("givenName") != null) {
                                firstName = name.get("givenName").asText();
                            }
                            if (name.get("familyName") != null) {
                                lastName = name.get("familyName").asText();
                            };
                        }
                    }
                }

                // get emailaddresses array
                JsonNode emailsNode = contact.get("emailAddresses");
                if (emailsNode.isArray()) {
                    for (JsonNode email : emailsNode) {
                        if (email.get("metadata").get("primary").asText() == "true") {
                            if (email.get("value") != null) {
                                emailAddress = email.get("value").asText();
                            }
                        }
                    }
                }

                // get phoneNumber
                JsonNode phonesNode = contact.get("phoneNumbers");
                if (phonesNode.isArray()) {
                    for (JsonNode phone : phonesNode) {
                        if (phone.get("metadata").get("primary").asText() == "true") {
                            if (phone.get("value") != null) {
                                phoneNumber = phone.get("value").asText();
                            }
                        }
                    }
                }

                // get organizations array
                JsonNode orgsNode = contact.get("organizations");
                if (orgsNode.isArray()) {
                    for (JsonNode org : orgsNode) {
                        if (org.get("metadata").get("primary").asText() == "true") {
                            if (org.get("name") != null) {
                                organization = org.get("name").asText();
                            }
                            if (org.get("title") != null) {
                                role = org.get("title").asText();
                            }
                        }
                    }
                }

                Contact c = new Contact(userId, googleId, firstName, lastName, emailAddress, phoneNumber, organization, role);
                contactRepository.save(c);
            }
        }
    }
}
