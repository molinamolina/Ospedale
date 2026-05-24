package core.controllers.interfaces;

import core.controllers.responses.Response;
import java.util.HashMap;

public interface IDoctorController {
    Response registerDoctor(HashMap<String, String> data);
    Response updateDoctor(HashMap<String, String> data);
    Response getDoctorInfo(long doctorId);
    Response getAllDoctors();
    Response getDoctorsBySpecialty(String specialtyDisplayName);
}
