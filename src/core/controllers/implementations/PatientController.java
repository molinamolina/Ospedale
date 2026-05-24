package core.controllers.implementations;

import core.controllers.interfaces.IPatientController;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IPatientDAO;
import core.models.dao.interfaces.IUserDAO;
import core.models.entities.Patient;
import core.utils.EntitySerializer;
import core.validators.implementations.PatientValidator;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PatientController implements IPatientController {

    private final IPatientDAO patientDAO;
    private final IUserDAO userDAO;

    public PatientController(IPatientDAO patientDAO, IUserDAO userDAO) {
        this.patientDAO = patientDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Response registerPatient(HashMap<String, String> data) {
        Response validation = new PatientValidator(userDAO, false).validate(data);
        if (validation.getStatus() != StatusCode.OK) {
            return validation;
        }

        Patient patient = buildPatient(data, Long.parseLong(data.get("id")));
        patientDAO.save(patient);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("patient", EntitySerializer.serializeUser(patient));
        return new Response("Patient registered successfully.", StatusCode.CREATED, responseData);
    }

    @Override
    public Response updatePatient(HashMap<String, String> data) {
        long currentId = Long.parseLong(data.get("currentId"));
        Patient existing = patientDAO.findById(currentId);
        if (existing == null) {
            return new Response("Patient not found.", StatusCode.NOT_FOUND);
        }

        data.put("id", String.valueOf(currentId));
        Response validation = new PatientValidator(userDAO, true).validate(data);
        if (validation.getStatus() != StatusCode.OK) {
            return validation;
        }

        Patient updated = buildPatient(data, currentId);
        patientDAO.save(updated);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("patient", EntitySerializer.serializeUser(updated));
        return new Response("Patient updated successfully.", StatusCode.OK, responseData);
    }

    @Override
    public Response getPatientInfo(long patientId) {
        Patient patient = patientDAO.findById(patientId);
        if (patient == null) {
            return new Response("Patient not found.", StatusCode.NOT_FOUND);
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("patient", EntitySerializer.serializeUser(patient));
        return new Response("Patient information retrieved.", StatusCode.OK, responseData);
    }

    @Override
    public Response getAllPatients() {
        List<HashMap<String, Object>> patients = new ArrayList<>();
        for (Patient patient : patientDAO.findAll()) {
            patients.add(EntitySerializer.serializePatientOption(patient));
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("patients", patients);
        return new Response("Patients retrieved.", StatusCode.OK, responseData);
    }

    private Patient buildPatient(HashMap<String, String> data, long id) {
        return new Patient(
                id,
                data.get("username"),
                data.get("firstname"),
                data.get("lastname"),
                data.get("password"),
                data.get("email"),
                LocalDate.parse(data.get("birthdate")),
                Boolean.parseBoolean(data.get("gender")),
                Long.parseLong(data.get("phone")),
                data.get("address")
        );
    }
}
