package shareit.item.dto;

import lombok.Getter;
import lombok.Setter;
import shareit.user.dto.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentDtoIn {
    @NotBlank(groups = Marker.OnCreate.class)
    @Size(max = 50, groups = Marker.OnCreate.class)
    private String text;
}
