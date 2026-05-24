package core.models.dao.implementations;

import core.models.dao.interfaces.IUserDAO;
import core.models.entities.User;
import core.repositories.DataStorage;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    private final DataStorage storage;

    public UserDAO(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save(User user) {
        for (int i = 0; i < storage.getUsers().size(); i++) {
            if (storage.getUsers().get(i).getId() == user.getId()) {
                storage.getUsers().set(i, user);
                storage.notifyDataChanged();
                return;
            }
        }
        storage.addUser(user);
    }

    @Override
    public User findById(long id) {
        for (User user : storage.getUsers()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        for (User user : storage.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.getUsers());
    }

    @Override
    public boolean existsById(long id) {
        return findById(id) != null;
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }
}
