package shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shareit.user.dto.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ItemDtoIn {
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(max = 50, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String name;
    @NotEmpty(groups = Marker.OnCreate.class)
    @Size(max = 50, groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    private String description;
    @NotNull(groups = Marker.OnCreate.class)
    private Boolean available;
    private long requestId;

}