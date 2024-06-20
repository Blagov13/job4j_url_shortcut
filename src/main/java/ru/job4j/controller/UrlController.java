package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.service.UrlService;

import java.util.Map;

@RestController
@RequestMapping("/convert")
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/convert")
    public ResponseEntity<?> convertUrl(@RequestBody Map<String, String> urlInfo) {
        String originalUrl = urlInfo.get("url");
        String shortUrl = urlService.convertToShortUrl(originalUrl);
        return ResponseEntity.ok(Map.of("code", shortUrl));
    }

    @GetMapping("/redirect/{code}")
    public ResponseEntity<?> redirectUrl(@PathVariable String code) {
        String originalUrl = urlService.getOriginalUrl(code);
        return ResponseEntity.status(302).header("Location", originalUrl).build();
    }

    @GetMapping("/statistic")
    public ResponseEntity<?> getUrlStatistics() {
        return ResponseEntity.ok(urlService.getStatistics());
    }
}