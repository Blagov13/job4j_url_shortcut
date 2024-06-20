package ru.job4j.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Url;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);

    Optional<Url> findByOriginalUrl(String originalUrl);

    /*Увеличение счетчика реализуем в базе данных,
    чтобы исключить возможность возникновения "Гонки данных"*/
    @Transactional
    @Modifying
    @Query("UPDATE Url u SET u.visitCount = u.visitCount + 1 WHERE u.id = :id")
    void incrementVisitCount(@Param("id") Long id);
}
