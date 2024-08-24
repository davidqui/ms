package com.quijano.authserver.services;

import com.quijano.authserver.dto.TokenDto;
import com.quijano.authserver.dto.UserDto;
import com.quijano.authserver.entities.UserEntity;
import com.quijano.authserver.helpers.JwtHelper;
import com.quijano.authserver.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;
    private static final String USER_EXCEPTION_MSG = "Error tokenDto authserver user";

    /**
     * @param user
     * @return
     */
    @Override
    public TokenDto login(UserDto user) {
        final var userFromDB = this.userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG));
        this.validPassword(user, userFromDB);
        return TokenDto.builder().accessToken(this.jwtHelper.createToken(userFromDB.getUsername())).build();
    }

    /**
     * @param token
     * @return
     */
    @Override
    public TokenDto validateToken(TokenDto token) {
        if (this.jwtHelper.validateToken(token.getAccessToken())) {
            return TokenDto.builder().accessToken(token.getAccessToken()).build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG);
    }

    /**
     * Método de utilidad para verificar que el passwordEncoder esté realizando el match de manera correcta.
     *
     * @param userDto
     * @param userEntity
     */
    private void validPassword(UserDto userDto, UserEntity userEntity) {
        if (!this.passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG);
        }
    }
}
