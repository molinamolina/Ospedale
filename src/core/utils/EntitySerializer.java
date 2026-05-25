package core.utils;

import core.models.entities.Prescription;
import core.controllers.RoomType;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Hospitalization;
import core.models.entities.Patient;
import core.models.entities.User;
import core.models.entities.Administrator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class EntitySerializer {

    private EntitySerializer() {
    }

    public static HashMap<String, Object> serializeUser(User user) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", user.getId());
        data.put("username", user.getUsername());
        data.put("firstname", user.getFirstname());
        data.put("lastname", user.getLastname());
        if (user instanceof Administrator) {
            data.put("type", "admin");
        } else if (user instanceof Patient patient) {
            data.put("type", "patient");
            data.put("email", patient.getEmail());
            data.put("birthdate", patient.getBirthdate().toString());
            data.put("gender", patient.isGender());
            data.put("phone", patient.getPhone());
            data.put("address", patient.getAddress());
        } else if (user instanceof Doctor doctor) {
            data.put("type", "doctor");
            data.put("specialty", doctor.getSpecialty().name());
            data.put("licenceNumber", doctor.getLicenceNumber());
            data.put("assignedOffice", doctor.getAssignedOffice());
        }
        return data;
    }

    public static HashMap<String, Object> serializeAppointment(Appointment appointment) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", appointment.getId());
        data.put("patientId", appointment.getPatient().getId());
        data.put("patientName", appointment.getPatient().getFirstname() + " " + appointment.getPatient().getLastname());
        data.put("doctorId", appointment.getDoctor() != null ? appointment.getDoctor().getId() : null);
        data.put("doctorName", appointment.getDoctor() != null
                ? appointment.getDoctor().getFirstname() + " " + appointment.getDoctor().getLastname()
                : "");
        data.put("specialty", appointment.getSpecialty().name());
        data.put("datetime", appointment.getDatetime().toString());
        data.put("date", appointment.getDatetime().toLocalDate().toString());
        data.put("time", appointment.getDatetime().toLocalTime().toString().substring(0, 5));
        data.put("reason", appointment.getReason());
        data.put("type", appointment.isType() ? "In-person" : "Remote");
        data.put("status", appointment.getStatus().name());
        data.put("diagnosis", appointment.getDiagnosis());
        data.put("observations", appointment.getObservations());
        data.put("recommendedTreatment", appointment.getRecommendedTreatment());
        data.put("followUp", appointment.getFollowUp());
        return data;
    }

    public static List<HashMap<String, Object>> serializeAppointments(List<Appointment> appointments) {
        List<HashMap<String, Object>> rows = new ArrayList<>();
        for (Appointment appointment : appointments) {
            rows.add(serializeAppointment(appointment));
        }
        return rows;
    }

    public static HashMap<String, Object> serializeHospitalization(Hospitalization hospitalization) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", hospitalization.getId());
        data.put("patientId", hospitalization.getPatient().getId());
        data.put("patientName", hospitalization.getPatient().getFirstname() + " " + hospitalization.getPatient().getLastname());
        data.put("doctorId", hospitalization.getDoctor().getId());
        data.put("doctorName", hospitalization.getDoctor().getFirstname() + " " + hospitalization.getDoctor().getLastname());
        data.put("date", hospitalization.getDate().toString());
        data.put("reason", hospitalization.getReason());
        data.put("roomType", hospitalization.getRoomType().name());
        data.put("observations", hospitalization.getObservations());
        data.put("status", hospitalization.getStatus().name());
        return data;
    }

    public static List<HashMap<String, Object>> serializeHospitalizations(List<Hospitalization> hospitalizations) {
        List<HashMap<String, Object>> rows = new ArrayList<>();
        for (Hospitalization hospitalization : hospitalizations) {
            rows.add(serializeHospitalization(hospitalization));
        }
        return rows;
    }

    public static HashMap<String, Object> serializeDoctorOption(Doctor doctor) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", doctor.getId());
        data.put("label", doctor.getFirstname() + " " + doctor.getLastname());
        data.put("specialty", doctor.getSpecialty().name());
        return data;
    }

    public static HashMap<String, Object> serializePatientOption(Patient patient) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", patient.getId());
        data.put("label", patient.getFirstname() + " " + patient.getLastname());
        return data;
    }

    public static HashMap<String, Object> serializePrescription(Prescription prescription) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("medicationName", prescription.getMedicationName());
        data.put("dose", prescription.getDose());
        data.put("administrationRoute", prescription.getAdministrationRoute());
        data.put("treatmentDuration", prescription.getTreatmentDuration());
        data.put("additionalInstructions", prescription.getAdditionalInstructions());
        data.put("frequency", prescription.getFrequency());
        return data;
    }

    public static List<String> serializeRoomTypes() {
        List<String> roomTypes = new ArrayList<>();
        for (RoomType roomType : RoomType.values()) {
            roomTypes.add(roomType.name());
        }
        return roomTypes;
    }
}
