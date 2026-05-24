package core.tests;

import core.ApplicationContext;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import java.util.HashMap;

public class ControllerSmokeTest {

    public static void main(String[] args) {
        ApplicationContext context = ApplicationContext.getInstance();
        int passed = 0;
        int failed = 0;

        Response login = context.getAuthController().login("admin_root", "Admin@1234");
        if (login.getStatus() == StatusCode.OK) {
            passed++;
            System.out.println("PASS login admin");
        } else {
            failed++;
            System.out.println("FAIL login admin: " + login.getMessage());
        }

        Response badLogin = context.getAuthController().login("admin_root", "wrong");
        if (badLogin.getStatus() == StatusCode.BAD_REQUEST) {
            passed++;
            System.out.println("PASS bad login rejected");
        } else {
            failed++;
            System.out.println("FAIL bad login");
        }

        HashMap<String, String> appointmentData = new HashMap<>();
        appointmentData.put("patientId", "112234567890");
        appointmentData.put("date", "2026-06-01");
        appointmentData.put("time", "09:00");
        appointmentData.put("reason", "General checkup");
        appointmentData.put("appointmentType", "true");
        appointmentData.put("byDoctor", "true");
        appointmentData.put("doctorId", "111234567890");

        Response appointment = context.getAppointmentController().requestAppointment(appointmentData);
        if (appointment.getStatus() == StatusCode.CREATED) {
            passed++;
            System.out.println("PASS request appointment");
        } else {
            failed++;
            System.out.println("FAIL request appointment: " + appointment.getMessage());
        }

        System.out.println("Smoke test finished. Passed=" + passed + " Failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }
}
