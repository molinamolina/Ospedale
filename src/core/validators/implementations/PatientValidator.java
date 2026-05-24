/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.entities.Patient;

/**
 *
 * @author Victus
 */
public class PatientValidator extends UserValidator<Patient> {

    @Override
    public Response validate(Patient patient) {
        if (patient.getId() == 0)
            return new Response("Please provide a valid ID.", StatusCode.BAD_REQUEST);
        if (!isValidEmail(patient.getEmail()))
            return new Response("Please provide a valid email.", StatusCode.BAD_REQUEST);
        if (!isValidPhone(String.valueOf(patient.getPhone())))
            return new Response("Please provide a valid phone number.", StatusCode.BAD_REQUEST);
        return new Response("Valid", StatusCode.OK);
    }
}
