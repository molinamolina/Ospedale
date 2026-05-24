/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.entities.Appointment;

/**
 *
 * @author Victus
 */
public class AppointmentValidator extends DateTimeValidator<Appointment> {

    @Override
    public Response validate(Appointment appointment) {
        if (appointment.getDatetime() == null)
            return new Response("Please provide a valid date.", StatusCode.BAD_REQUEST);
        if (!isValidDate(appointment.getDatetime().toLocalDate().toString()))
            return new Response("Please provide a valid date.", StatusCode.BAD_REQUEST);
        if (!isValidTime(appointment.getDatetime().toLocalTime().toString()))
            return new Response("Please provide a valid time.", StatusCode.BAD_REQUEST);
        return new Response("Valid", StatusCode.OK);
    }
}
