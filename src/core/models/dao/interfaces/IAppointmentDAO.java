package core.models.dao.interfaces;

import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import java.util.List;

public interface IAppointmentDAO {
    void save(Appointment appointment);
    Appointment findById(String id);
    List<Appointment> findAll();
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
}
