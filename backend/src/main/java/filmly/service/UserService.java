package filmly.service;

import filmly.model.User;
import java.util.List;

public interface UserService {

    User getUserById(Long userId);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(Long userId, User updatedUser);

    void deleteUser(Long userId);

}
