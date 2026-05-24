package core.controllers.implementations;

import core.controllers.interfaces.IAuthController;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IUserDAO;
import core.models.entities.Administrator;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.models.entities.User;
import core.utils.EntitySerializer;
import java.util.HashMap;

public class AuthController implements IAuthController {

    private final IUserDAO userDAO;

    public AuthController(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Response login(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return new Response("Bad or nonexistent credentials.", StatusCode.BAD_REQUEST);
        }

        HashMap<String, Object> data = EntitySerializer.serializeUser(user);
        if (user instanceof Administrator) {
            data.put("role", "admin");
        } else if (user instanceof Doctor) {
            data.put("role", "doctor");
        } else if (user instanceof Patient) {
            data.put("role", "patient");
        }
        return new Response("Successfully logged in.", StatusCode.OK, data);
    }
}
