package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IUserDAO;
import core.utils.DateTimeManager;
import core.validators.interfaces.IValidator;
import java.util.HashMap;
import java.util.regex.Pattern;

public class PatientValidator implements IValidator<HashMap<String, String>> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9]+@[A-Za-z0-9]+\\.com$");
    private final IUserDAO userDAO;
    private final boolean isUpdate;

    public PatientValidator(IUserDAO userDAO, boolean isUpdate) {
        this.userDAO = userDAO;
        this.isUpdate = isUpdate;
    }

    @Override
    public Response validate(HashMap<String, String> data) {
        Response userValidation = new UserValidator(userDAO, isUpdate).validate(data);
        if (userValidation.getStatus() != StatusCode.OK) {
            return userValidation;
        }
        if (data.get("phone") == null || !data.get("phone").matches("\\d{10}")) {
            return new Response("Phone must have exactly 10 digits.", StatusCode.BAD_REQUEST);
        }
        if (data.get("email") == null || !EMAIL_PATTERN.matcher(data.get("email")).matches()) {
            return new Response("Email must follow the format XXXXX@XXXXX.com.", StatusCode.BAD_REQUEST);
        }
        if (data.get("birthdate") == null || !DateTimeManager.isValidDate(data.get("birthdate"))) {
            return new Response("Birthdate must be valid and follow yyyy-MM-dd.", StatusCode.BAD_REQUEST);
        }
        if (data.get("gender") == null || data.get("gender").isBlank()) {
            return new Response("Gender must be selected.", StatusCode.BAD_REQUEST);
        }
        if (data.get("firstname") == null || data.get("firstname").isBlank()
                || data.get("lastname") == null || data.get("lastname").isBlank()
                || data.get("address") == null || data.get("address").isBlank()) {
            return new Response("All patient fields are required.", StatusCode.BAD_REQUEST);
        }
        return new Response("Valid patient data.", StatusCode.OK);
    }
}
