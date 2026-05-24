package core.models.dao.interfaces;

import core.models.entities.Hospitalization;
import core.models.entities.Patient;
import java.util.List;

public interface IHospitalizationDAO {
    void save(Hospitalization hospitalization);
    Hospitalization findById(String id);
    List<Hospitalization> findAll();
    List<Hospitalization> findByPatient(Patient patient);
}
