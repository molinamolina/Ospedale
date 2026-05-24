package core.models.dao.interfaces;

import core.controllers.Prescription;
import java.util.List;

public interface IMedicationDAO {
    void save(Prescription prescription, String appointmentId);
    List<Prescription> findByAppointmentId(String appointmentId);
}
