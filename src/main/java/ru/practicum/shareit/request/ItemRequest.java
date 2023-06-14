package ru.practicum.shareit.request;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
//что то не понял зачем нам этот класс)))
@Entity
@Table(name = "requests")
@NoArgsConstructor
@EnableJpaRepositories(basePackages = "ru.practicum")
public class ItemRequest {
    @Id
    private long id;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    public ItemRequest(long id, String description, User requestor) {
        this.id = id;
        this.description = description;
        this.requestor = requestor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getRequestor() {
        return requestor;
    }

    public void setRequestor(User requestor) {
        this.requestor = requestor;
    }
}
