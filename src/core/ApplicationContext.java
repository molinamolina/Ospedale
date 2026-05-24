package core;

import core.controllers.implementations.AppointmentController;
import core.controllers.implementations.AuthController;
import core.controllers.implementations.DoctorController;
import core.controllers.implementations.HospitalizationController;
import core.controllers.implementations.PatientController;
import core.controllers.interfaces.IAppointmentController;
import core.controllers.interfaces.IAuthController;
import core.controllers.interfaces.IDoctorController;
import core.controllers.interfaces.IHospitalizationController;
import core.controllers.interfaces.IPatientController;
import core.models.dao.implementations.AppointmentDAO;
import core.models.dao.implementations.DoctorDAO;
import core.models.dao.implementations.HospitalizationDAO;
import core.models.dao.implementations.MedicationDAO;
import core.models.dao.implementations.PatientDAO;
import core.models.dao.implementations.UserDAO;
import core.models.dao.interfaces.IAppointmentDAO;
import core.models.dao.interfaces.IDoctorDAO;
import core.models.dao.interfaces.IHospitalizationDAO;
import core.models.dao.interfaces.IMedicationDAO;
import core.models.dao.interfaces.IPatientDAO;
import core.models.dao.interfaces.IUserDAO;
import core.services.AppointmentAvailabilityService;
import core.models.entities.Administrator;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.models.entities.User;
import core.repositories.DataStorage;
import core.utils.JSONLoader;

public final class ApplicationContext {

    private static ApplicationContext instance;

    private final DataStorage storage;
    private final IUserDAO userDAO;
    private final IPatientDAO patientDAO;
    private final IDoctorDAO doctorDAO;
    private final IAppointmentDAO appointmentDAO;
    private final IHospitalizationDAO hospitalizationDAO;
    private final IMedicationDAO medicationDAO;
    private final AppointmentAvailabilityService availabilityService;
    private final IAuthController authController;
    private final IPatientController patientController;
    private final IDoctorController doctorController;
    private final IAppointmentController appointmentController;
    private final IHospitalizationController hospitalizationController;
    private User currentUser;

    private ApplicationContext() {
        storage = DataStorage.getInstance();
        JSONLoader.userLoad();

        userDAO = new UserDAO(storage);
        patientDAO = new PatientDAO(storage);
        doctorDAO = new DoctorDAO(storage);
        appointmentDAO = new AppointmentDAO(storage);
        hospitalizationDAO = new HospitalizationDAO(storage);
        medicationDAO = new MedicationDAO(appointmentDAO);
        availabilityService = new AppointmentAvailabilityService(appointmentDAO);

        authController = new AuthController(userDAO);
        patientController = new PatientController(patientDAO, userDAO);
        doctorController = new DoctorController(doctorDAO, userDAO);
        appointmentController = new AppointmentController(
                appointmentDAO,
                patientDAO,
                doctorDAO,
                medicationDAO,
                availabilityService
        );
        hospitalizationController = new HospitalizationController(
                hospitalizationDAO,
                patientDAO,
                doctorDAO,
                appointmentDAO,
                appointmentController
        );
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public DataStorage getStorage() {
        return storage;
    }

    public IAuthController getAuthController() {
        return authController;
    }

    public IPatientController getPatientController() {
        return patientController;
    }

    public IDoctorController getDoctorController() {
        return doctorController;
    }

    public IAppointmentController getAppointmentController() {
        return appointmentController;
    }

    public IHospitalizationController getHospitalizationController() {
        return hospitalizationController;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User findUserById(long id) {
        for (User user : storage.getUsers()) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User user : storage.getUsers()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
