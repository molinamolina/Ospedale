package core.models.dao.implementations;

import core.models.dao.interfaces.IPatientDAO;
import core.models.entities.Patient;
import core.models.entities.User;
import core.repositories.DataStorage;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO implements IPatientDAO {

    private final DataStorage storage;

    public PatientDAO(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Patient patient) {
        for (int i = 0; i < storage.getUsers().size(); i++) {
            User user = storage.getUsers().get(i);
            if (user.getId() == patient.getId()) {
                storage.getUsers().set(i, patient);
                storage.notifyDataChanged();
                return;
            }
        }
        storage.addUser(patient);
    }

    @Override
    public Patient findById(long id) {
        for (User user : storage.getUsers()) {
            if (user.getId() == id && user instanceof Patient patient) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> patients = new ArrayList<>();
        for (User user : storage.getUsers()) {
            if (user instanceof Patient patient) {
                patients.add(patient);
            }
        }
        return patients;
    }
}
