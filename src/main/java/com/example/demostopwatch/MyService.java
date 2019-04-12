package com.example.demostopwatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class MyService {

    @Autowired
    RestTemplate restTemplate;


    public GithubUser getUser(String login) {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("param1", "toto");

        //Set the headers you need send
        final HttpHeaders headers = new HttpHeaders();
        headers.set("param1", "toto");

        //Create a new HttpEntity
        final HttpEntity<String> requestEntity = new HttpEntity<>(headers);


        ResponseEntity<GithubUser> responseEntity = this.restTemplate.exchange("https://api.github.com/users/" + login, HttpMethod.GET, requestEntity, GithubUser.class);

        //ResponseEntity<GithubUser> responseEntity = this.restTemplate.getForEntity("https://api.github.com/users/" + login, GithubUser.class, uriVariables);

        return responseEntity.getBody();
    }

}
