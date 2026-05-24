package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.utils.DateTimeManager;
import core.validators.interfaces.IValidator;

public class DateTimeValidator implements IValidator<String> {

    private final boolean validateTime;

    public DateTimeValidator(boolean validateTime) {
        this.validateTime = validateTime;
    }

    @Override
    public Response validate(String value) {
        if (value == null || value.isBlank()) {
            return new Response("Date/time value is required.", StatusCode.BAD_REQUEST);
        }
        if (!DateTimeManager.isValidDate(value)) {
            return new Response("Invalid date format. Use yyyy-MM-dd.", StatusCode.BAD_REQUEST);
        }
        if (validateTime && !DateTimeManager.isValidTime(value)) {
            return new Response("Invalid time format. Use HH:mm with minutes in 00, 15, 30 or 45.", StatusCode.BAD_REQUEST);
        }
        return new Response("Valid date/time.", StatusCode.OK);
    }
}
