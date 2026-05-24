package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IUserDAO;
import core.utils.SpecialtyMapper;
import core.validators.interfaces.IValidator;
import java.util.HashMap;
import java.util.regex.Pattern;

public class DoctorValidator implements IValidator<HashMap<String, String>> {

    private static final Pattern LICENCE_PATTERN = Pattern.compile("^L-\\d{10} MTL$");
    private static final Pattern OFFICE_PATTERN = Pattern.compile("^O-\\d{3}$");
    private final IUserDAO userDAO;
    private final boolean isUpdate;

    public DoctorValidator(IUserDAO userDAO, boolean isUpdate) {
        this.userDAO = userDAO;
        this.isUpdate = isUpdate;
    }

    @Override
    public Response validate(HashMap<String, String> data) {
        Response userValidation = new UserValidator(userDAO, isUpdate).validate(data);
        if (userValidation.getStatus() != StatusCode.OK) {
            return userValidation;
        }
        if (data.get("licenceNumber") == null || !LICENCE_PATTERN.matcher(data.get("licenceNumber")).matches()) {
            return new Response("Licence number must follow L-XXXXXXXXXX MTL.", StatusCode.BAD_REQUEST);
        }
        if (data.get("assignedOffice") == null || !OFFICE_PATTERN.matcher(data.get("assignedOffice")).matches()) {
            return new Response("Assigned office must follow O-XXX.", StatusCode.BAD_REQUEST);
        }
        try {
            SpecialtyMapper.fromDisplayName(data.get("specialty"));
        } catch (Exception ex) {
            return new Response("Invalid specialty.", StatusCode.BAD_REQUEST);
        }
        if (data.get("firstname") == null || data.get("firstname").isBlank()
                || data.get("lastname") == null || data.get("lastname").isBlank()) {
            return new Response("All doctor fields are required.", StatusCode.BAD_REQUEST);
        }
        return new Response("Valid doctor data.", StatusCode.OK);
    }
}
