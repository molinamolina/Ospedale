/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.repositories;

/**
 *
 * @author Victus
 */
import core.models.entities.*;
import java.util.ArrayList;
public class DataStorage {
    private static DataStorage instance;
        
    private ArrayList<User> users;
    private ArrayList<Appointment> appointments;
    private ArrayList<Hospitalization> hospitalizations;

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
    
}
