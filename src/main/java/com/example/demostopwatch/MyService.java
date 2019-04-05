package com.example.demostopwatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MyService {

    @Autowired
    RestTemplate restTemplate;


    public GithubUser getUser(String login) {
        ResponseEntity<GithubUser> responseEntity = this.restTemplate.getForEntity("https://api.github.com/users/" + login, GithubUser.class);

        return responseEntity.getBody();
    }

}
