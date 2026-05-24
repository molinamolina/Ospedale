package core.models.dao.implementations;

import core.models.dao.interfaces.IDoctorDAO;
import core.models.entities.Doctor;
import core.models.entities.Specialty;
import core.models.entities.User;
import core.repositories.DataStorage;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO implements IDoctorDAO {

    private final DataStorage storage;

    public DoctorDAO(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Doctor doctor) {
        for (int i = 0; i < storage.getUsers().size(); i++) {
            User user = storage.getUsers().get(i);
            if (user.getId() == doctor.getId()) {
                storage.getUsers().set(i, doctor);
                storage.notifyDataChanged();
                return;
            }
        }
        storage.addUser(doctor);
    }

    @Override
    public Doctor findById(long id) {
        for (User user : storage.getUsers()) {
            if (user.getId() == id && user instanceof Doctor doctor) {
                return doctor;
            }
        }
        return null;
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = new ArrayList<>();
        for (User user : storage.getUsers()) {
            if (user instanceof Doctor doctor) {
                doctors.add(doctor);
            }
        }
        return doctors;
    }

    @Override
    public List<Doctor> findBySpecialty(Specialty specialty) {
        List<Doctor> doctors = new ArrayList<>();
        for (User user : storage.getUsers()) {
            if (user instanceof Doctor doctor && doctor.getSpecialty() == specialty) {
                doctors.add(doctor);
            }
        }
        return doctors;
    }
}
