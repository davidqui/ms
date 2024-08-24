package com.quijano.authserver.services;

import com.quijano.authserver.dto.TokenDto;
import com.quijano.authserver.dto.UserDto;

public interface AuthService {

    TokenDto login(UserDto user);
    TokenDto validateToken(TokenDto tokenDto);
}
