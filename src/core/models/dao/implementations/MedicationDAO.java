package core.models.dao.implementations;

import core.controllers.Prescription;
import core.models.dao.interfaces.IAppointmentDAO;
import core.models.dao.interfaces.IMedicationDAO;
import core.models.entities.Appointment;
import java.util.ArrayList;
import java.util.List;

public class MedicationDAO implements IMedicationDAO {

    private final IAppointmentDAO appointmentDAO;

    public MedicationDAO(IAppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    @Override
    public void save(Prescription prescription, String appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment != null) {
            appointment.addPrescription(prescription);
            appointmentDAO.save(appointment);
        }
    }

    @Override
    public List<Prescription> findByAppointmentId(String appointmentId) {
        Appointment appointment = appointmentDAO.findById(appointmentId);
        if (appointment == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(appointment.getPrescriptions());
    }
}
