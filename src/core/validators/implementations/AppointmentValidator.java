package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.entities.Appointment;
import core.utils.DateTimeManager;
import core.validators.interfaces.IValidator;

public class AppointmentValidator implements IValidator<Appointment> {

    @Override
    public Response validate(Appointment appointment) {
        if (appointment == null) {
            return new Response("Appointment is required.", StatusCode.BAD_REQUEST);
        }
        String date = appointment.getDatetime().toLocalDate().toString();
        String time = appointment.getDatetime().toLocalTime().toString().substring(0, 5);
        if (!DateTimeManager.isValidDate(date) || !DateTimeManager.isValidTime(time)) {
            return new Response("Invalid appointment date or time.", StatusCode.BAD_REQUEST);
        }
        return new Response("Valid appointment.", StatusCode.OK);
    }
}
