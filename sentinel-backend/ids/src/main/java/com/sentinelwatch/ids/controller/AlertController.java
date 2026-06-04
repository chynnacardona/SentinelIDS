package com.sentinelwatch.ids.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentinelwatch.ids.model.Alert;
import com.sentinelwatch.ids.repository.AlertRepository;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {
    //REPOSITORY INSTANCE
    @Autowired //this tells springboot to automatically instantiate and inject repository object
    private AlertRepository alertRepository;

    // Inject Spring's WebSocket broker messaging template
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    //CREATE operation: Receives threat data from Python and saves it.
    //@RequestBody maps the incoming Python JSON directly into a Java Alert object.
    @PostMapping
    public ResponseEntity<Alert> receiveAlert(@RequestBody Alert alert) {
        Alert savedAlert = alertRepository.save(alert); //This single line takes your alert object and inputs it into the H2 database
        // BROADCAST: Pushes the new alert automatically to all live web dashboard browsers!
        messagingTemplate.convertAndSend("/topic/alerts", savedAlert);
        return new ResponseEntity<>(savedAlert, HttpStatus.CREATED);
    }

    // READ operation: Fetches all tracked threats for your Day 5 frontend dashboard.
    @GetMapping
    public ResponseEntity<List<Alert>> getAllAlerts() {
        return ResponseEntity.ok(alertRepository.findAll());
    }
}