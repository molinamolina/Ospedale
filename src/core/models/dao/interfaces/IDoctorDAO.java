package core.models.dao.interfaces;

import core.models.entities.Doctor;
import core.models.entities.Specialty;
import java.util.List;

public interface IDoctorDAO {
    void save(Doctor doctor);
    Doctor findById(long id);
    List<Doctor> findAll();
    List<Doctor> findBySpecialty(Specialty specialty);
}
