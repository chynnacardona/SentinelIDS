package com.sentinelwatch.ids.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sentinelwatch.ids.repository.AlertRepository;

@Controller
public class DashboardController {

    @Autowired
    private AlertRepository alertRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Pre-load existing logs from H2 so the table isn't blank on startup
        model.addAttribute("alerts", alertRepository.findAll());
        return "dashboard"; // Maps exactly to templates/dashboard.html
    }
}