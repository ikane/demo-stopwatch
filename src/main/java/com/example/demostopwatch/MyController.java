package com.example.demostopwatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private MyService myService;

    @GetMapping(path = "/users/{login}")
    public ResponseEntity<GithubUser> getUser(@PathVariable String login) {
        return ResponseEntity.ok(this.myService.getUser(login));
    }
}
