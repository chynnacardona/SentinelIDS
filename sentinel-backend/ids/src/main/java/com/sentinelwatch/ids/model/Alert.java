package com.sentinelwatch.ids.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String ipAddress;
    private String threatType;
    private String timestamp;
    private int attemptCount; // New field added to catch and broadcast the current step failure tally

    // No-args constructor
    public Alert() {}

    // Overloaded helper constructor
    public Alert(String ipAddress, String threatType, String timestamp, int attemptCount) {
        this.ipAddress = ipAddress;
        this.threatType = threatType;
        this.timestamp = timestamp;
        this.attemptCount = attemptCount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getThreatType() { return threatType; }
    public void setThreatType(String threatType) { this.threatType = threatType; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }
}