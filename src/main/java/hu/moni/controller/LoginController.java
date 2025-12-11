package hu.moni.controller;

import hu.moni.config.JwtService;
import hu.moni.service.TwoFactorAuthService;
import hu.moni.model.User;
import hu.moni.model.dto.AuthRequest;
import hu.moni.model.dto.LoginResponse;
import hu.moni.model.dto.TwoFactorRequest;
import hu.moni.model.dto.TwoFactorSetupResponse;
import hu.moni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moni")
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TwoFactorAuthService twoFactorAuthService;

    // -------------------------------
    // 1) LOGIN (username + password)
    // -------------------------------
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest req) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, null));
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow();

        if (!user.isTwoFactorEnabled()) {

            String jwt = jwtService.generateToken(
                    org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .roles(user.getRole().name())
                            .build()
            );

            return ResponseEntity.ok(new LoginResponse(false, jwt));
        }

        return ResponseEntity.ok(new LoginResponse(true, null));
    }


    // -------------------------------
    // 2) 2FA LOGIN (TOTP kód)
    // -------------------------------
    @PostMapping("/login/2fa")
    public ResponseEntity<LoginResponse> verify2fa(@RequestBody TwoFactorRequest req) {

        User user = userRepository.findByEmail(req.getUsername())
                .orElseThrow();

        if (!user.isTwoFactorEnabled()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, null));
        }

        boolean valid = twoFactorAuthService.verifyCode(
                user.getTotpSecret(),
                req.getCode()
        );

        if (!valid) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new LoginResponse(true, null));
        }

        String jwt = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build()
        );

        return ResponseEntity.ok(new LoginResponse(false, jwt));
    }

    //2fa regisztráció
    @PostMapping("/login/setup2fa")
    public ResponseEntity<TwoFactorSetupResponse> setup2faLogin(@RequestBody AuthRequest req) {

        // 1) Jelszó ellenőrzés
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow();

        // 2) Ha már van secret → nem kell setup
        if (user.getTotpSecret() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new TwoFactorSetupResponse("already_setup", null));
        }

        // 3) Secret generálás
        String secret = twoFactorAuthService.generateSecret();
        user.setTotpSecret(secret);
        user.setTwoFactorEnabled(true);
        userRepository.save(user);

        String qrUrl = twoFactorAuthService.generateQrUrl(user.getEmail(), secret);

        return ResponseEntity.ok(new TwoFactorSetupResponse(secret, qrUrl));
    }


}

