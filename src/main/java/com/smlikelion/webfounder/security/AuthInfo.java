package com.smlikelion.webfounder.security;

import com.smlikelion.webfounder.admin.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AuthInfo {
    private String token;
    private String accountId;
    private List<Role> roles;
}
