package io.akitect.healthcare.apigateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/patient")
    public Mono<ResponseEntity<Map<String, String>>> patientFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Patient service is currently unavailable. Please try again later.");
        response.put("status", "error");
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }

    @GetMapping("/encounter")
    public Mono<ResponseEntity<Map<String, String>>> encounterFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Encounter service is currently unavailable. Please try again later.");
        response.put("status", "error");
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }

    // Add similar fallback methods for other services

    @GetMapping("/**")
    public Mono<ResponseEntity<Map<String, String>>> defaultFallback() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "The requested service is currently unavailable. Please try again later.");
        response.put("status", "error");
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }
}