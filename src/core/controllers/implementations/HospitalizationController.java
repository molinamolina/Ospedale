package core.controllers.implementations;

import core.controllers.RoomType;
import core.controllers.interfaces.IAppointmentController;
import core.controllers.interfaces.IHospitalizationController;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IAppointmentDAO;
import core.models.dao.interfaces.IDoctorDAO;
import core.models.dao.interfaces.IHospitalizationDAO;
import core.models.dao.interfaces.IPatientDAO;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Hospitalization;
import core.models.entities.Patient;
import core.models.enums.HospitalizationStatus;
import core.utils.DateTimeManager;
import core.utils.EntitySerializer;
import core.utils.IDGen;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HospitalizationController implements IHospitalizationController {

    private final IHospitalizationDAO hospitalizationDAO;
    private final IPatientDAO patientDAO;
    private final IDoctorDAO doctorDAO;
    private final IAppointmentDAO appointmentDAO;
    private final IAppointmentController appointmentController;

    public HospitalizationController(
            IHospitalizationDAO hospitalizationDAO,
            IPatientDAO patientDAO,
            IDoctorDAO doctorDAO,
            IAppointmentDAO appointmentDAO,
            IAppointmentController appointmentController
    ) {
        this.hospitalizationDAO = hospitalizationDAO;
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
        this.appointmentDAO = appointmentDAO;
        this.appointmentController = appointmentController;
    }

    @Override
    public Response requestHospitalization(HashMap<String, String> data) {
        long patientId = Long.parseLong(data.get("patientId"));
        long doctorId = Long.parseLong(data.get("doctorId"));
        Patient patient = patientDAO.findById(patientId);
        Doctor doctor = doctorDAO.findById(doctorId);

        if (patient == null || doctor == null) {
            return new Response("Patient or doctor not found.", StatusCode.NOT_FOUND);
        }
        if (!DateTimeManager.isValidDate(data.get("date"))) {
            return new Response("Invalid hospitalization date.", StatusCode.BAD_REQUEST);
        }

        RoomType roomType = RoomType.valueOf(data.get("roomType").toUpperCase());
        String hospitalizationId = IDGen.generateHospitalizationId(String.valueOf(patientId));
        Hospitalization hospitalization = new Hospitalization(
                hospitalizationId,
                patient,
                doctor,
                LocalDate.parse(data.get("date")),
                data.get("reason"),
                roomType,
                data.get("observations")
        );
        hospitalizationDAO.save(hospitalization);
        return successResponse("Hospitalization requested successfully.", hospitalization);
    }

    @Override
    public Response approveHospitalization(HashMap<String, String> data) {
        Hospitalization hospitalization = hospitalizationDAO.findById(data.get("hospitalizationId"));
        if (hospitalization == null) {
            return new Response("Hospitalization not found.", StatusCode.NOT_FOUND);
        }
        if (hospitalization.getStatus() != HospitalizationStatus.REQUESTED) {
            return new Response("Only requested hospitalizations can be approved.", StatusCode.BAD_REQUEST);
        }
        hospitalization.setStatus(HospitalizationStatus.ONGOING);
        hospitalizationDAO.save(hospitalization);
        return successResponse("Hospitalization approved successfully.", hospitalization);
    }

    @Override
    public Response denyHospitalization(HashMap<String, String> data) {
        Hospitalization hospitalization = hospitalizationDAO.findById(data.get("hospitalizationId"));
        if (hospitalization == null) {
            return new Response("Hospitalization not found.", StatusCode.NOT_FOUND);
        }
        hospitalization.setStatus(HospitalizationStatus.CANCELED);
        hospitalizationDAO.save(hospitalization);
        return successResponse("Hospitalization denied successfully.", hospitalization);
    }

    @Override
    public Response generateFromAppointment(HashMap<String, String> data) {
        Appointment appointment = appointmentDAO.findById(data.get("appointmentId"));
        if (appointment == null) {
            return new Response("Appointment not found.", StatusCode.NOT_FOUND);
        }

        HashMap<String, String> completeData = new HashMap<>();
        completeData.put("appointmentId", appointment.getId());
        completeData.put("doctorId", String.valueOf(appointment.getDoctor().getId()));
        completeData.put("diagnosis", data.get("reason"));
        completeData.put("observations", data.get("observations"));
        completeData.put("recommendedTreatment", "");
        completeData.put("followUp", "");
        Response completeResponse = appointmentController.completeAppointment(completeData);
        if (completeResponse.getStatus() != StatusCode.OK) {
            return completeResponse;
        }

        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();
        String hospitalizationId = IDGen.generateHospitalizationId(String.valueOf(patient.getId()));
        Hospitalization hospitalization = new Hospitalization(
                hospitalizationId,
                patient,
                doctor,
                LocalDate.parse(data.get("date")),
                data.get("reason"),
                RoomType.valueOf(data.get("roomType").toUpperCase()),
                data.get("observations"),
                HospitalizationStatus.ONGOING
        );
        hospitalizationDAO.save(hospitalization);
        return successResponse("Hospitalization generated successfully.", hospitalization);
    }

    @Override
    public Response cancelHospitalization(HashMap<String, String> data) {
        Hospitalization hospitalization = hospitalizationDAO.findById(data.get("hospitalizationId"));
        if (hospitalization == null) {
            return new Response("Hospitalization not found.", StatusCode.NOT_FOUND);
        }
        hospitalization.setStatus(HospitalizationStatus.CANCELED);
        hospitalizationDAO.save(hospitalization);
        return successResponse("Hospitalization canceled successfully.", hospitalization);
    }

    @Override
    public Response getDoctorHospitalizations(long doctorId) {
        Doctor doctor = doctorDAO.findById(doctorId);
        if (doctor == null) {
            return new Response("Doctor not found.", StatusCode.NOT_FOUND);
        }
        List<Hospitalization> hospitalizations = new ArrayList<>();
        for (Hospitalization hospitalization : hospitalizationDAO.findAll()) {
            if (hospitalization.getDoctor().getId() == doctorId) {
                hospitalizations.add(hospitalization);
            }
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("hospitalizations", EntitySerializer.serializeHospitalizations(hospitalizations));
        return new Response("Hospitalizations retrieved.", StatusCode.OK, responseData);
    }

    private Response successResponse(String message, Hospitalization hospitalization) {
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("hospitalization", EntitySerializer.serializeHospitalization(hospitalization));
        return new Response(message, StatusCode.OK, responseData);
    }
}
