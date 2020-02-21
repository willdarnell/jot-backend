package jotbackend.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jotbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

@Service
public class ContactService {

    @Autowired
    private UserRepository contactRepository;

    public String addUserContacts(Integer userId, String accessToken) {
        String uri_base = "https://people.googleapis.com/v1/people/me/connections?personFields=names,emailAddresses&pageToken=";
        String nextPageToken = null;
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
                        Map<String, String> map = mapper.readValue(result.getBody(), Map.class);
                        // System.out.println(result.getBody());
                        nextPageToken = map.get("nextPageToken");
                        System.out.println(nextPageToken);
                    }
                } catch (HttpStatusCodeException e) {
                    System.out.println(e);
                } catch (JsonProcessingException e) {
                    System.out.println(e);
            }
        } while (nextPageToken != null);

        return "Success";
    }
}
