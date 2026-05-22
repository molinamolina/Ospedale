/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.validators.implementations;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.entities.Appointment;
import core.validators.interfaces.*; // even though there's literally only one file in there...
/**
 *
 * @author Victus
 */
public class AppointmentValidator implements IValidator<Appointment> {

    @Override
    public Response validate(Appointment appointment) {
        if (appointment.getDate() == null) {
            return new Response("Please provide a valid date.", StatusCode.BAD_REQUEST);
        }
        return new Response("Valid", StatusCode.OK);
    }
}
