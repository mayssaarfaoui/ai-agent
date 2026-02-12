package com.aimanager.agent.dto;

import com.aimanager.agent.actors.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private final String id;
    private final String name;
    private final String email;
    private final String phone;

    public static UserDto of(User user) {
        return user == null ? null : new UserDto(user);
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
    }
}
