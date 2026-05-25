package core.models.dao.interfaces;

import core.models.entities.Prescription;
import java.util.List;

public interface IMedicationDAO {
    void save(Prescription prescription, String appointmentId);
    List<Prescription> findByAppointmentId(String appointmentId);
}
