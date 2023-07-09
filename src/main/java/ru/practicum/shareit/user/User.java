package ru.practicum.shareit.user;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@EnableJpaRepositories(basePackages = "ru.practicum")
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;

    public User(long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public User(long userId) {
        this.id = userId;
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