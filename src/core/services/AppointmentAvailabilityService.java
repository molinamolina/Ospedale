package core.services;

import core.models.dao.interfaces.IAppointmentDAO;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.enums.AppointmentStatus;
import java.time.LocalDateTime;

public class AppointmentAvailabilityService {

    private final IAppointmentDAO appointmentDAO;

    public AppointmentAvailabilityService(IAppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    public boolean isDoctorAvailable(Doctor doctor, LocalDateTime datetime, String excludeAppointmentId) {
        LocalDateTime end = datetime.plusMinutes(15);
        for (Appointment appointment : appointmentDAO.findByDoctor(doctor)) {
            if (excludeAppointmentId != null && appointment.getId().equals(excludeAppointmentId)) {
                continue;
            }
            if (appointment.getStatus() == AppointmentStatus.CANCELED) {
                continue;
            }
            LocalDateTime start = appointment.getDatetime();
            LocalDateTime appointmentEnd = start.plusMinutes(15);
            if (datetime.isBefore(appointmentEnd) && end.isAfter(start)) {
                return false;
            }
        }
        return true;
    }
}
