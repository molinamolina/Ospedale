package core.controllers.interfaces;

import core.controllers.responses.Response;
import java.util.HashMap;

public interface IAppointmentController {
    Response requestAppointment(HashMap<String, String> data);
    Response acceptAppointment(HashMap<String, String> data);
    Response completeAppointment(HashMap<String, String> data);
    Response cancelAppointment(HashMap<String, String> data);
    Response rescheduleAppointment(HashMap<String, String> data);
    Response prescribeMedications(HashMap<String, String> data);
    Response getPatientAppointments(long patientId);
    Response getDoctorAppointments(long doctorId, boolean pendingOnly);
    Response getPatientAppointmentsTable(long patientId);
}
