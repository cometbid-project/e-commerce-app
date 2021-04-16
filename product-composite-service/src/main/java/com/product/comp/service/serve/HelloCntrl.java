package com.product.comp.service.serve;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloCntrl {

    @GetMapping("/hello/get")
    public Mono<String> getHello() {
        return Mono.just("Getting back..");
    }

    @PostMapping("/hello/post")
    public Mono<String> postHello() {
        return Mono.just("Posting back..");
    }

    
}
