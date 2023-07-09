package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.jpa.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.jpa.UserRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
public class JpaTests {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindAllByNameOrDescription() {
        // Создаем тестовые данные
        Item item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("q Descrtesttion 1");
        item1.setAvailable(false);
        User user = new User();
        user.setEmail("dsa.aw@mail.ru");
        user.setName("123");
        userRepository.save(user);
        item1.setOwner(user);

        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Another Item");
        item2.setDescription("Another test Description");
        item2.setAvailable(true);
        item2.setOwner(user);
        itemRepository.save(item2);


        // Вызываем метод findAllByNameOrDescription с поисковым запросом и без пагинации
        String searchString = "Test";
        PageRequest pageRequest = PageRequest.of(0, 99);
        List<Item> o = itemRepository.findAll();
        Page<Item> result = itemRepository.findAllByNameOrDescription(searchString.toLowerCase(), pageRequest);

        // Проверяем результаты
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
    }
}