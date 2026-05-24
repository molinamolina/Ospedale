package core.controllers.implementations;

import core.controllers.interfaces.IDoctorController;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IDoctorDAO;
import core.models.dao.interfaces.IUserDAO;
import core.models.entities.Doctor;
import core.models.entities.Specialty;
import core.utils.EntitySerializer;
import core.utils.SpecialtyMapper;
import core.validators.implementations.DoctorValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorController implements IDoctorController {

    private final IDoctorDAO doctorDAO;
    private final IUserDAO userDAO;

    public DoctorController(IDoctorDAO doctorDAO, IUserDAO userDAO) {
        this.doctorDAO = doctorDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Response registerDoctor(HashMap<String, String> data) {
        Response validation = new DoctorValidator(userDAO, false).validate(data);
        if (validation.getStatus() != StatusCode.OK) {
            return validation;
        }

        Doctor doctor = buildDoctor(data, Long.parseLong(data.get("id")));
        doctorDAO.save(doctor);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("doctor", EntitySerializer.serializeUser(doctor));
        return new Response("Doctor registered successfully.", StatusCode.CREATED, responseData);
    }

    @Override
    public Response updateDoctor(HashMap<String, String> data) {
        long currentId = Long.parseLong(data.get("currentId"));
        Doctor existing = doctorDAO.findById(currentId);
        if (existing == null) {
            return new Response("Doctor not found.", StatusCode.NOT_FOUND);
        }

        data.put("id", String.valueOf(currentId));
        Response validation = new DoctorValidator(userDAO, true).validate(data);
        if (validation.getStatus() != StatusCode.OK) {
            return validation;
        }

        Doctor updated = buildDoctor(data, currentId);
        doctorDAO.save(updated);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("doctor", EntitySerializer.serializeUser(updated));
        return new Response("Doctor updated successfully.", StatusCode.OK, responseData);
    }

    @Override
    public Response getDoctorInfo(long doctorId) {
        Doctor doctor = doctorDAO.findById(doctorId);
        if (doctor == null) {
            return new Response("Doctor not found.", StatusCode.NOT_FOUND);
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("doctor", EntitySerializer.serializeUser(doctor));
        return new Response("Doctor information retrieved.", StatusCode.OK, responseData);
    }

    @Override
    public Response getAllDoctors() {
        List<HashMap<String, Object>> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDAO.findAll()) {
            doctors.add(EntitySerializer.serializeDoctorOption(doctor));
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("doctors", doctors);
        return new Response("Doctors retrieved.", StatusCode.OK, responseData);
    }

    @Override
    public Response getDoctorsBySpecialty(String specialtyDisplayName) {
        Specialty specialty = SpecialtyMapper.fromDisplayName(specialtyDisplayName);
        List<HashMap<String, Object>> doctors = new ArrayList<>();
        for (Doctor doctor : doctorDAO.findBySpecialty(specialty)) {
            doctors.add(EntitySerializer.serializeDoctorOption(doctor));
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("doctors", doctors);
        return new Response("Doctors retrieved.", StatusCode.OK, responseData);
    }

    private Doctor buildDoctor(HashMap<String, String> data, long id) {
        return new Doctor(
                id,
                data.get("username"),
                data.get("firstname"),
                data.get("lastname"),
                data.get("password"),
                SpecialtyMapper.fromDisplayName(data.get("specialty")),
                data.get("licenceNumber"),
                data.get("assignedOffice")
        );
    }
}
