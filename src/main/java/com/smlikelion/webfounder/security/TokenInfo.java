package com.smlikelion.webfounder.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TokenInfo {
    private String token;
    private Long expiredTime;
}
