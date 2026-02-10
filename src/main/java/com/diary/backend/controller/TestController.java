package com.diary.backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public Map<String, String> test(@RequestParam String message) {
        Map<String, String> result = new HashMap<>();
        result.put("received", message);
        return result;
    }
}
