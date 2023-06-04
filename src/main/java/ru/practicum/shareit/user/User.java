package ru.practicum.shareit.user;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class User {
    private long id;
    @NotBlank(groups = Marker.OnCreate.class)
    private String name;
    @NotEmpty(groups = Marker.OnCreate.class)
    @Email(groups = Marker.class)
    private String email;

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}