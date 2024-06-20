package ru.job4j.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.job4j.model.Url;
import ru.job4j.repository.UrlRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UrlServiceTest {
    @InjectMocks
    private UrlService urlService;

    @Mock
    private UrlRepository urlRepository;

    private static final String ORIGINAL_URL = "https://www.example.com";
    private static final String SHORT_URL = "abc123";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testConvertToShortUrl_NewUrl() {
        when(urlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.empty());
        when(urlRepository.findByShortUrl(SHORT_URL)).thenReturn(Optional.empty());
        String shortUrl = urlService.convertToShortUrl(ORIGINAL_URL);
        assertEquals(SHORT_URL.length(), shortUrl.length());
        verify(urlRepository, times(1)).save(any(Url.class));
    }

    @Test
    public void testConvertToShortUrl_ExistingUrl() {
        Url existingUrl = new Url();
        existingUrl.setOriginalUrl(ORIGINAL_URL);
        existingUrl.setShortUrl(SHORT_URL);
        when(urlRepository.findByOriginalUrl(ORIGINAL_URL)).thenReturn(Optional.of(existingUrl));
        String shortUrl = urlService.convertToShortUrl(ORIGINAL_URL);
        assertEquals(SHORT_URL, shortUrl);
        verify(urlRepository, never()).save(any(Url.class));
    }

    @Test
    public void testGetOriginalUrl_InvalidShortUrl() {
        when(urlRepository.findByShortUrl("invalidShortUrl")).thenReturn(Optional.empty());
        try {
            urlService.getOriginalUrl("invalidShortUrl");
        } catch (NoSuchElementException e) {
            assertEquals("Короткий URL не найден", e.getMessage());
        }
    }

    @Test
    public void testGetStatistics() {
        List<Url> urls = new ArrayList<>();
        Url url1 = new Url();
        url1.setOriginalUrl("https://www.example1.com");
        url1.setShortUrl("abc123");
        url1.setVisitCount(5L);
        Url url2 = new Url();
        url2.setOriginalUrl("https://www.example2.com");
        url2.setShortUrl("def456");
        url2.setVisitCount(10L);
        urls.add(url1);
        urls.add(url2);
        when(urlRepository.findAll()).thenReturn(urls);
        List<Map<String, Object>> statistics = urlService.getStatistics();
        assertEquals(2, statistics.size());
        assertEquals("https://www.example1.com", statistics.get(0).get("url"));
        assertEquals(5L, statistics.get(0).get("total"));
        assertEquals("https://www.example2.com", statistics.get(1).get("url"));
        assertEquals(10L, statistics.get(1).get("total"));
    }
}