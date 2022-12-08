package com.itmo.se.recommendationservice.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;


@Value
@AllArgsConstructor(onConstructor_ = @JsonCreator)
@Builder
public class AppUser {
    @With
    Long userId;
    @With
    UserRole role;

}
