package com.quijano.authserver.controllers;

import com.quijano.authserver.dto.TokenDto;
import com.quijano.authserver.dto.UserDto;
import com.quijano.authserver.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "login") // password administradorDB
    public ResponseEntity<TokenDto> jwtCreate(@RequestBody UserDto user){
        return ResponseEntity.ok(this.authService.login(user));
    }

    @PostMapping(path = "jwt")
    public ResponseEntity<TokenDto> jwtValidate(@RequestHeader String accessToken){
        return ResponseEntity.ok(this.authService.validateToken(TokenDto.builder().accessToken(accessToken).build()));
    }
}
