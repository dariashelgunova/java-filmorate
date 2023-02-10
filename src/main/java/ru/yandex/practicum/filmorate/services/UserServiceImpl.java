package ru.yandex.practicum.filmorate.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.models.User;
import ru.yandex.practicum.filmorate.repos.UserRepository;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        return userRepository.update(user);
    }

    @Override
    public void startFriendship(int friendInitializerId, int newFriendId) {
        userRepository.startFriendship(friendInitializerId, newFriendId);
    }

    @Override
    public void endFriendship(int friendInitializerId, int friendIdToDelete) {
        userRepository.endFriendship(friendInitializerId, friendIdToDelete);
    }

    @Override
    public List<User> findCommonFriends(int user1Id, int user2Id) {
        return userRepository.findCommonFriends(user1Id, user2Id);
    }

    @Override
    public List<User> findFriendsByUserId(Integer id) {
        return userRepository.findFriendsByUserId(id);
    }
}
