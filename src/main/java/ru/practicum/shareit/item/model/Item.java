package ru.practicum.shareit.item.model;

import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long userId;
    private ItemRequest request;

    public Item(long id, String name, String description, Boolean available, long userId, ItemRequest request) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.userId = userId;
        this.request = request;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ItemRequest getRequest() {
        return request;
    }

    public void setRequest(ItemRequest request) {
        this.request = request;
    }
}