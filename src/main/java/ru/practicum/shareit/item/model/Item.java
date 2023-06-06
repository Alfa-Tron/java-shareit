package ru.practicum.shareit.item.model;

import ru.practicum.shareit.request.ItemRequest;

public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private ItemRequest request;

    public Item(long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }


    public ItemRequest getRequest() {
        return request;
    }

    public void setRequest(ItemRequest request) {
        this.request = request;
    }

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }
}