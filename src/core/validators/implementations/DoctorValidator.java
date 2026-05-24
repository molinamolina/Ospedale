/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.validators.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.entities.Doctor;

/**
 *
 * @author Victus
 */
public class DoctorValidator extends UserValidator<Doctor> {

    // License: L-XXXXXXXXXX MTL
    public static boolean isValidLicense(String license) {
        if (license == null) return false;
        return license.matches("L-\\d{10} MTL");
    }

    // Office: O-XXX (3 dígitos)
    public static boolean isValidOffice(String office) {
        if (office == null) return false;
        return office.matches("O-\\d{3}");
    }

    @Override
    public Response validate(Doctor doctor) {
        if (doctor.getId() == 0)
            return new Response("Please provide a valid ID.", StatusCode.BAD_REQUEST);
        if (!isValidLicense(doctor.getLicenceNumber()))
            return new Response("Please provide a valid licence number.", StatusCode.BAD_REQUEST);
        if (!isValidOffice(doctor.getAssignedOffice()))
            return new Response("Please provide a valid office.", StatusCode.BAD_REQUEST);
        return new Response("Valid", StatusCode.OK);
    }
}
