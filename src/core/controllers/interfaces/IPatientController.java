package core.controllers.interfaces;

import core.controllers.responses.Response;
import java.util.HashMap;

public interface IPatientController {
    Response registerPatient(HashMap<String, String> data);
    Response updatePatient(HashMap<String, String> data);
    Response getPatientInfo(long patientId);
    Response getAllPatients();
}
