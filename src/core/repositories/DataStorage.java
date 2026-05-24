/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.repositories;

import core.models.entities.Appointment;
import core.models.entities.Hospitalization;
import core.models.entities.User;
import core.observers.implementations.DataSubject;
import java.util.ArrayList;

public class DataStorage extends DataSubject {

    private static DataStorage instance;

    private final ArrayList<User> users;
    private final ArrayList<Appointment> appointments;
    private final ArrayList<Hospitalization> hospitalizations;

    private DataStorage() {
        users = new ArrayList<>();
        appointments = new ArrayList<>();
        hospitalizations = new ArrayList<>();
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    public void addUser(User user) {
        users.add(user);
        notifyObservers();
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        notifyObservers();
    }

    public void addHospitalization(Hospitalization hospitalization) {
        hospitalizations.add(hospitalization);
        notifyObservers();
    }

    public void notifyDataChanged() {
        notifyObservers();
    }
}
