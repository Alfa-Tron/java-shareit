package shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookingDtoIn {
    @NotNull
    private final Long itemId;
    @FutureOrPresent
    @NotNull
    private final LocalDateTime start;
    @Future
    @NotNull
    private final LocalDateTime end;

    public BookingDtoIn(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
