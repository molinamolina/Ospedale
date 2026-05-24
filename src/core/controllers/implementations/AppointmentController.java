package core.controllers.implementations;

import core.controllers.Prescription;
import core.controllers.interfaces.IAppointmentController;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.dao.interfaces.IAppointmentDAO;
import core.models.dao.interfaces.IDoctorDAO;
import core.models.dao.interfaces.IMedicationDAO;
import core.models.dao.interfaces.IPatientDAO;
import core.services.AppointmentAvailabilityService;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.models.entities.Specialty;
import core.models.enums.AppointmentStatus;
import core.utils.DateTimeManager;
import core.utils.EntitySerializer;
import core.utils.IDGen;
import core.utils.SpecialtyMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

public class AppointmentController implements IAppointmentController {

    private final IAppointmentDAO appointmentDAO;
    private final IPatientDAO patientDAO;
    private final IDoctorDAO doctorDAO;
    private final IMedicationDAO medicationDAO;
    private final AppointmentAvailabilityService availabilityService;

    public AppointmentController(
            IAppointmentDAO appointmentDAO,
            IPatientDAO patientDAO,
            IDoctorDAO doctorDAO,
            IMedicationDAO medicationDAO,
            AppointmentAvailabilityService availabilityService
    ) {
        this.appointmentDAO = appointmentDAO;
        this.patientDAO = patientDAO;
        this.doctorDAO = doctorDAO;
        this.medicationDAO = medicationDAO;
        this.availabilityService = availabilityService;
    }

    @Override
    public Response requestAppointment(HashMap<String, String> data) {
        long patientId = Long.parseLong(data.get("patientId"));
        Patient patient = patientDAO.findById(patientId);
        if (patient == null) {
            return new Response("Patient not found.", StatusCode.NOT_FOUND);
        }

        if (!DateTimeManager.isValidDate(data.get("date"))) {
            return new Response("Invalid appointment date.", StatusCode.BAD_REQUEST);
        }
        if (!DateTimeManager.isValidTime(data.get("time"))) {
            return new Response("Invalid appointment time.", StatusCode.BAD_REQUEST);
        }

        LocalDateTime datetime = LocalDateTime.of(
                LocalDate.parse(data.get("date")),
                LocalTime.parse(data.get("time"))
        );

        boolean byDoctor = Boolean.parseBoolean(data.get("byDoctor"));
        Doctor doctor;
        Specialty specialty;

        if (byDoctor) {
            long doctorId = Long.parseLong(data.get("doctorId"));
            doctor = doctorDAO.findById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found.", StatusCode.NOT_FOUND);
            }
            specialty = doctor.getSpecialty();
        } else {
            specialty = SpecialtyMapper.fromDisplayName(data.get("specialty"));
            doctor = findAvailableDoctor(specialty, datetime, null);
            if (doctor == null) {
                return new Response("No doctor available for the requested specialty and time.", StatusCode.BAD_REQUEST);
            }
        }

        if (!availabilityService.isDoctorAvailable(doctor, datetime, null)) {
            return new Response("Doctor is not available at the requested time.", StatusCode.BAD_REQUEST);
        }

        String appointmentId = IDGen.generateAppointmentId(String.valueOf(patientId));
        boolean appointmentType = Boolean.parseBoolean(data.get("appointmentType"));
        Appointment appointment = new Appointment(
                appointmentId,
                patient,
                doctor,
                specialty,
                datetime,
                data.get("reason"),
                appointmentType
        );

        patient.addAppointment(appointment);
        doctor.addAppointment(appointment);
        appointmentDAO.save(appointment);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("appointment", EntitySerializer.serializeAppointment(appointment));
        return new Response("Appointment requested successfully.", StatusCode.CREATED, responseData);
    }

    @Override
    public Response acceptAppointment(HashMap<String, String> data) {
        Appointment appointment = appointmentDAO.findById(data.get("appointmentId"));
        if (appointment == null) {
            return new Response("Appointment not found.", StatusCode.NOT_FOUND);
        }
        if (appointment.getStatus() != AppointmentStatus.REQUESTED) {
            return new Response("Only requested appointments can be accepted.", StatusCode.BAD_REQUEST);
        }

        long doctorId = Long.parseLong(data.get("doctorId"));
        if (appointment.getDoctor().getId() != doctorId) {
            return new Response("Doctor is not assigned to this appointment.", StatusCode.FORBIDDEN);
        }

        appointment.setStatus(AppointmentStatus.PENDING);
        appointmentDAO.save(appointment);
        return successAppointmentResponse("Appointment accepted successfully.", appointment);
    }

    @Override
    public Response completeAppointment(HashMap<String, String> data) {
        Appointment appointment = appointmentDAO.findById(data.get("appointmentId"));
        if (appointment == null) {
            return new Response("Appointment not found.", StatusCode.NOT_FOUND);
        }
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return new Response("Only pending appointments can be completed.", StatusCode.BAD_REQUEST);
        }

        long doctorId = Long.parseLong(data.get("doctorId"));
        if (appointment.getDoctor().getId() != doctorId) {
            return new Response("Doctor is not assigned to this appointment.", StatusCode.FORBIDDEN);
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setDiagnosis(data.get("diagnosis"));
        appointment.setObservations(data.get("observations"));
        appointment.setRecommendedTreatment(data.get("recommendedTreatment"));
        appointment.setFollowUp(data.get("followUp"));
        appointmentDAO.save(appointment);
        return successAppointmentResponse("Appointment completed successfully.", appointment);
    }

    @Override
    public Response cancelAppointment(HashMap<String, String> data) {
        Appointment appointment = appointmentDAO.findById(data.get("appointmentId"));
        if (appointment == null) {
            return new Response("Appointment not found.", StatusCode.NOT_FOUND);
        }
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            return new Response("Completed appointments cannot be canceled.", StatusCode.BAD_REQUEST);
        }

        appointment.setStatus(AppointmentStatus.CANCELED);
        if (data.get("observation") != null && !data.get("observation").isBlank()) {
            appointment.setObservations(data.get("observation"));
        }
        appointmentDAO.save(appointment);
        return successAppointmentResponse("Appointment canceled successfully.", appointment);
    }

    @Override
    public Response rescheduleAppointment(HashMap<String, String> data) {
        Appointment appointment = appointmentDAO.findById(data.get("appointmentId"));
        if (appointment == null) {
            return new Response("Appointment not found.", StatusCode.NOT_FOUND);
        }

        if (!DateTimeManager.isValidTime(data.get("newTime"))) {
            return new Response("Invalid appointment time.", StatusCode.BAD_REQUEST);
        }

        LocalTime newTime = LocalTime.parse(data.get("newTime"));
        LocalDateTime newDatetime = LocalDateTime.of(appointment.getDatetime().toLocalDate(), newTime);
        Doctor doctor = appointment.getDoctor();

        if (!availabilityService.isDoctorAvailable(doctor, newDatetime, appointment.getId())) {
            return new Response("Doctor is not available at the requested time.", StatusCode.BAD_REQUEST);
        }

        String updatedReason = appointment.getReason();
        if (data.get("reason") != null && !data.get("reason").isBlank()) {
            updatedReason = updatedReason + " | Reschedule: " + data.get("reason");
        }
        appointment.setReason(updatedReason);
        appointment.setDatetime(newDatetime);
        appointmentDAO.save(appointment);
        return successAppointmentResponse("Appointment rescheduled successfully.", appointment);
    }

    @Override
    public Response prescribeMedications(HashMap<String, String> data) {
        Appointment appointment = appointmentDAO.findById(data.get("appointmentId"));
        if (appointment == null) {
            return new Response("Appointment not found.", StatusCode.NOT_FOUND);
        }
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            return new Response("Medications can only be prescribed for pending appointments.", StatusCode.BAD_REQUEST);
        }

        long doctorId = Long.parseLong(data.get("doctorId"));
        if (appointment.getDoctor().getId() != doctorId) {
            return new Response("Doctor is not assigned to this appointment.", StatusCode.FORBIDDEN);
        }

        Prescription prescription = new Prescription(
                appointment,
                data.get("medicationName"),
                Double.parseDouble(data.get("dose")),
                data.get("administrationRoute"),
                Integer.parseInt(data.get("treatmentDuration")),
                data.get("additionalInstructions"),
                Integer.parseInt(data.get("frequency"))
        );
        medicationDAO.save(prescription, appointment.getId());

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("prescription", EntitySerializer.serializePrescription(prescription));
        return new Response("Medication prescribed successfully.", StatusCode.CREATED, responseData);
    }

    @Override
    public Response getPatientAppointments(long patientId) {
        Patient patient = patientDAO.findById(patientId);
        if (patient == null) {
            return new Response("Patient not found.", StatusCode.NOT_FOUND);
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("appointments", EntitySerializer.serializeAppointments(appointmentDAO.findByPatient(patient)));
        return new Response("Appointments retrieved.", StatusCode.OK, responseData);
    }

    @Override
    public Response getDoctorAppointments(long doctorId, boolean pendingOnly) {
        Doctor doctor = doctorDAO.findById(doctorId);
        if (doctor == null) {
            return new Response("Doctor not found.", StatusCode.NOT_FOUND);
        }
        List<Appointment> appointments = appointmentDAO.findByDoctor(doctor);
        if (pendingOnly) {
            appointments = appointments.stream()
                    .filter(a -> a.getStatus() == AppointmentStatus.PENDING || a.getStatus() == AppointmentStatus.REQUESTED)
                    .toList();
        }
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("appointments", EntitySerializer.serializeAppointments(appointments));
        return new Response("Appointments retrieved.", StatusCode.OK, responseData);
    }

    @Override
    public Response getPatientAppointmentsTable(long patientId) {
        return getPatientAppointments(patientId);
    }

    private Doctor findAvailableDoctor(Specialty specialty, LocalDateTime datetime, String excludeAppointmentId) {
        for (Doctor doctor : doctorDAO.findBySpecialty(specialty)) {
            if (availabilityService.isDoctorAvailable(doctor, datetime, excludeAppointmentId)) {
                return doctor;
            }
        }
        return null;
    }

    private Response successAppointmentResponse(String message, Appointment appointment) {
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("appointment", EntitySerializer.serializeAppointment(appointment));
        return new Response(message, StatusCode.OK, responseData);
    }
}
