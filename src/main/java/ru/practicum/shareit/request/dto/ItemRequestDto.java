package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Getter
@Setter
public class ItemRequestDto {
    private long id;
    private String description;
    private Requester requestor;
    private LocalDateTime created;
    private List<Items> items = new ArrayList<>();

    public ItemRequestDto(long id, String description, User requestor, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.requestor = new Requester(requestor.getId());
        this.created = created;
    }

    public ItemRequestDto() {
        items = Collections.emptyList();
    }
    @Setter
    @Getter
    public static class Items {
        private long id;
        private String name;
        private String description;
        private boolean available;
        private long requestId;
    }
    @Getter
    @Setter
    static class Requester {
        private long id;

        public Requester(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }
}


