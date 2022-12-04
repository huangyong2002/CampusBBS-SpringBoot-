package com.huang.BBS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BBSApp {

    @GetMapping("/hello")
    String hello(){
        return "hello11111";
    }

    public static void main(String[] args) {
        SpringApplication.run(BBSApp.class,args);
    }
}
