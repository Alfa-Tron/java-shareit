package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Setter
@Getter
public class UserDto {
    private long id;
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(max = 50, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String name;
    @NotEmpty(groups = {Marker.OnCreate.class})
    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String email;

    public UserDto(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
