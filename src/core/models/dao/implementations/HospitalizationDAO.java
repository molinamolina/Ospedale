package core.models.dao.implementations;

import core.models.dao.interfaces.IHospitalizationDAO;
import core.models.entities.Hospitalization;
import core.models.entities.Patient;
import core.repositories.DataStorage;
import java.util.ArrayList;
import java.util.List;

public class HospitalizationDAO implements IHospitalizationDAO {

    private final DataStorage storage;

    public HospitalizationDAO(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Hospitalization hospitalization) {
        for (int i = 0; i < storage.getHospitalizations().size(); i++) {
            if (storage.getHospitalizations().get(i).getId().equals(hospitalization.getId())) {
                storage.getHospitalizations().set(i, hospitalization);
                storage.notifyDataChanged();
                return;
            }
        }
        storage.addHospitalization(hospitalization);
    }

    @Override
    public Hospitalization findById(String id) {
        for (Hospitalization hospitalization : storage.getHospitalizations()) {
            if (hospitalization.getId().equals(id)) {
                return hospitalization;
            }
        }
        return null;
    }

    @Override
    public List<Hospitalization> findAll() {
        return new ArrayList<>(storage.getHospitalizations());
    }

    @Override
    public List<Hospitalization> findByPatient(Patient patient) {
        List<Hospitalization> result = new ArrayList<>();
        for (Hospitalization hospitalization : storage.getHospitalizations()) {
            if (hospitalization.getPatient().getId() == patient.getId()) {
                result.add(hospitalization);
            }
        }
        return result;
    }
}
