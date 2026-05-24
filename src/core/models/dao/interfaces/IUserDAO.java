package core.models.dao.interfaces;

import core.models.entities.User;
import java.util.List;

public interface IUserDAO {
    void save(User user);
    User findById(long id);
    User findByUsername(String username);
    List<User> findAll();
    boolean existsById(long id);
    boolean existsByUsername(String username);
}
