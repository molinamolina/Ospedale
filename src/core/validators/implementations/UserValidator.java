package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IUserDAO;
import core.validators.interfaces.IValidator;
import java.util.HashMap;

public class UserValidator implements IValidator<HashMap<String, String>> {

    private final IUserDAO userDAO;
    private final boolean isUpdate;

    public UserValidator(IUserDAO userDAO, boolean isUpdate) {
        this.userDAO = userDAO;
        this.isUpdate = isUpdate;
    }

    @Override
    public Response validate(HashMap<String, String> data) {
        String idText = data.get("id");
        String username = data.get("username");
        String password = data.get("password");
        String confirmPassword = data.get("confirmPassword");

        if (username == null || username.isBlank()) {
            return new Response("Username is required.", StatusCode.BAD_REQUEST);
        }

        if (password == null || confirmPassword == null || !password.equals(confirmPassword)) {
            return new Response("Password and confirmation must match.", StatusCode.BAD_REQUEST);
        }

        if (!isUpdate) {
            if (idText == null || idText.isBlank()) {
                return new Response("User id is required.", StatusCode.BAD_REQUEST);
            }
            long id;
            try {
                id = Long.parseLong(idText);
            } catch (NumberFormatException ex) {
                return new Response("User id must be numeric.", StatusCode.BAD_REQUEST);
            }
            if (id <= 0 || String.valueOf(id).length() != 12) {
                return new Response("User id must be greater than 0 and have 12 digits.", StatusCode.BAD_REQUEST);
            }
            if (userDAO.existsById(id)) {
                return new Response("User id already exists.", StatusCode.BAD_REQUEST);
            }
        } else {
            long id = Long.parseLong(data.get("currentId"));
            if (userDAO.findByUsername(username) != null && userDAO.findByUsername(username).getId() != id) {
                return new Response("Username already exists.", StatusCode.BAD_REQUEST);
            }
            return new Response("Valid user data.", StatusCode.OK);
        }

        if (userDAO.existsByUsername(username)) {
            return new Response("Username already exists.", StatusCode.BAD_REQUEST);
        }

        return new Response("Valid user data.", StatusCode.OK);
    }
}
