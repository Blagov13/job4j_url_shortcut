package ru.job4j.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.jwt.JwtUtils;
import ru.job4j.model.Site;
import ru.job4j.repository.SiteRepository;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    SiteRepository siteRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/registration")
    public ResponseEntity<?> registerSite(@RequestBody Map<String, String> siteInfo) {
        String siteName = siteInfo.get("site");
        if (siteRepository.existsByLogin(siteName)) {
            return ResponseEntity.ok(Map.of("registration", false));
        }
        String login = java.util.UUID.randomUUID().toString();
        String password = java.util.UUID.randomUUID().toString();
        Site site = new Site();
        site.setLogin(login);
        site.setPassword(encoder.encode(password));
        siteRepository.save(site);

        return ResponseEntity.ok(Map.of(
                "registration", true,
                "login", login,
                "password", password
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginInfo) {
        String login = loginInfo.get("login");
        String password = loginInfo.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password));

        String jwt = jwtUtils.createToken(authentication.getName());

        return ResponseEntity.ok(Map.of("token", jwt));
    }
}
