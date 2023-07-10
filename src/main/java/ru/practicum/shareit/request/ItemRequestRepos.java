package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRequestRepos extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestor_IdOrderByCreatedDesc(long k);

    List<ItemRequest> findAllByOrderByCreatedAsc();

    Page<ItemRequest> findAllByRequestorNot(User currentUser, Pageable pageRequest);

}
