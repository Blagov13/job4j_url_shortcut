package ru.job4j.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.model.Url;
import ru.job4j.repository.UrlRepository;

import java.util.*;

@Service
public class UrlService {
    @Autowired
    private UrlRepository urlRepository;

    private static final String BASE_URL = "http://short.url/";
    private static final String CHAR_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SHORT_URL_LENGTH = 6;

    public String convertToShortUrl(String originalUrl) {
        Optional<Url> existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl.isPresent()) {
            return existingUrl.get().getShortUrl();
        }

        String shortUrl = generateShortUrl();
        while (urlRepository.findByShortUrl(shortUrl).isPresent()) {
            shortUrl = generateShortUrl();
        }

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);
        url.setVisitCount(0L);
        urlRepository.save(url);

        return shortUrl;
    }

    public String getOriginalUrl(String shortUrl) {
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new NoSuchElementException("Short URL not found"));
        urlRepository.incrementVisitCount(url.getId());

        return url.getOriginalUrl();
    }

    public List<Map<String, Object>> getStatistics() {
        List<Map<String, Object>> stats = new ArrayList<>();
        for (Url url : urlRepository.findAll()) {
            Map<String, Object> stat = new HashMap<>();
            stat.put("url", url.getOriginalUrl());
            stat.put("total", url.getVisitCount());
            stats.add(stat);
        }
        return stats;
    }

    private String generateShortUrl() {
        Random random = new Random();
        StringBuilder shortUrl = new StringBuilder(SHORT_URL_LENGTH);
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            shortUrl.append(CHAR_SET.charAt(random.nextInt(CHAR_SET.length())));
        }
        return shortUrl.toString();
    }
}
