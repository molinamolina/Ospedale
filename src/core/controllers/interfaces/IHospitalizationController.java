package core.controllers.interfaces;

import core.controllers.responses.Response;
import java.util.HashMap;

public interface IHospitalizationController {
    Response requestHospitalization(HashMap<String, String> data);
    Response approveHospitalization(HashMap<String, String> data);
    Response denyHospitalization(HashMap<String, String> data);
    Response generateFromAppointment(HashMap<String, String> data);
    Response cancelHospitalization(HashMap<String, String> data);
    Response getDoctorHospitalizations(long doctorId);
}
