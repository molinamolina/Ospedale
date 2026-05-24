package core.models.dao.interfaces;

import core.models.entities.Patient;
import java.util.List;

public interface IPatientDAO {
    void save(Patient patient);
    Patient findById(long id);
    List<Patient> findAll();
}
