package com.quijano.gateway.dtos;


public class TokenDto {

    public TokenDto(){

    }

    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
    }

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
