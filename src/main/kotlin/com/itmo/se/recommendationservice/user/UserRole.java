package com.itmo.se.recommendationservice.user;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum UserRole {
    USER,
    ADMIN;

    public List<UserRole> getPrecedingAndCurrent() {
        return Arrays.stream(UserRole.values())
                .filter(role -> role.compareTo(this) <= 0)
                .collect(Collectors.toList());
    }
}
