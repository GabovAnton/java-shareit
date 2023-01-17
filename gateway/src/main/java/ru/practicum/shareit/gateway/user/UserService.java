package ru.practicum.shareit.gateway.user;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.gateway.user.dto.UserDto;
import ru.practicum.shareit.gateway.user.dto.UserUpdateDto;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
@CacheConfig(cacheNames = {"users"})
public class UserService {

    private final UserClientFeign userClientFeign;

    @Cacheable
    public List<UserDto> getUsers() {

        return userClientFeign.getUsers();
    }

    @Cacheable
    public UserDto getUser(long id) {

        return userClientFeign.getUser(id);
    }

    @CachePut
    public UserDto create(@Valid UserDto userDto) {

        return userClientFeign.create(userDto);
    }

    @CachePut
    public UserDto update(long userId, @Valid UserUpdateDto userUpdateDto) {

        return userClientFeign.update(userId, userUpdateDto);
    }

    @CacheEvict
    public Boolean delete(long userToDeleteId) {

        return userClientFeign.delete(userToDeleteId);
    }

}
