package com.quijano.authserver.services;

import com.quijano.authserver.dto.TokenDto;
import com.quijano.authserver.dto.UserDto;
import com.quijano.authserver.entities.UserEntity;
import com.quijano.authserver.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
@AllArgsConstructor
public class AutServiceImpl implements AutService{

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private static final String USER_EXCEPTION_MSG = "Error tokenDto authserver user";

    /**
     * @param user
     * @return
     */
    @Override
    public TokenDto login(UserDto user) {
        return null;
    }

    /**
     * @param tokenDto
     * @return
     */
    @Override
    public TokenDto validateToken(TokenDto tokenDto) {
        return null;
    }

    /**
     * Metodo de utileria para verificar que el passworEncoder este realizando mach de manera correcta
     * @param user
     * @return
     */
    @Bean
    private void validPassword(UserDto userDto, UserEntity userEntity){
        if (!this.passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG);
        }
    }
}
