package core.models.dao.implementations;

import core.models.dao.interfaces.IAppointmentDAO;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.repositories.DataStorage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AppointmentDAO implements IAppointmentDAO {

    private final DataStorage storage;

    public AppointmentDAO(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void save(Appointment appointment) {
        for (int i = 0; i < storage.getAppointments().size(); i++) {
            if (storage.getAppointments().get(i).getId().equals(appointment.getId())) {
                storage.getAppointments().set(i, appointment);
                storage.notifyDataChanged();
                return;
            }
        }
        storage.addAppointment(appointment);
    }

    @Override
    public Appointment findById(String id) {
        for (Appointment appointment : storage.getAppointments()) {
            if (appointment.getId().equals(id)) {
                return appointment;
            }
        }
        return null;
    }

    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(storage.getAppointments());
    }

    @Override
    public List<Appointment> findByPatient(Patient patient) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : storage.getAppointments()) {
            if (appointment.getPatient().getId() == patient.getId()) {
                result.add(appointment);
            }
        }
        result.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return result;
    }

    @Override
    public List<Appointment> findByDoctor(Doctor doctor) {
        List<Appointment> result = new ArrayList<>();
        for (Appointment appointment : storage.getAppointments()) {
            if (appointment.getDoctor() != null && appointment.getDoctor().getId() == doctor.getId()) {
                result.add(appointment);
            }
        }
        result.sort(Comparator.comparing(Appointment::getDatetime).reversed());
        return result;
    }
}
