/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.utils;

/**
 *
 * @author Victus
 */
import core.repositories.DataStorage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import core.models.entities.*;

public class JSONLoader {

    public static void userLoad() {

        DataStorage storage = DataStorage.getInstance();
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/users.json")), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(content);
            JSONArray userArray = root.getJSONArray("users");

            for (int i = 0; i < userArray.length(); i++) {
                JSONObject jsonfile = userArray.getJSONObject(i);
                String type = jsonfile.getString("type");
                long id = jsonfile.getLong("id");
                String username = jsonfile.getString("username");
                String firstname = jsonfile.getString("firstname");
                String lastname = jsonfile.getString("lastname");
                String password = jsonfile.getString("password");

                if (type.equals("doctor")) {
                    String email = jsonfile.getString("email");
                    LocalDate birthdate = LocalDate.parse(jsonfile.getString("birthdate"));
                    boolean gender = jsonfile.getBoolean("gender");
                    long phone = jsonfile.getLong("phone");
                    String address = jsonfile.getString("address");
                    Specialty specialty = Specialty.valueOf(jsonfile.getString("specialty"));
                    String licenceNumber = jsonfile.getString("licenceNumber");
                    String assignedOffice = jsonfile.getString("assignedOffice");

                    Doctor doctor = new Doctor(id, username, firstname, lastname, password, specialty, licenceNumber, assignedOffice);
                    storage.getUsers().add(doctor);

                } else if (type.equals("patient")) {
                    String email = jsonfile.getString("email");
                    LocalDate birthdate = LocalDate.parse(jsonfile.getString("birthdate"));
                    boolean gender = jsonfile.getBoolean("gender");
                    long phone = jsonfile.getLong("phone");
                    String address = jsonfile.getString("address");

                    Patient patient = new Patient(id, username, firstname, lastname, password, email, birthdate, gender, phone, address);
                    storage.getUsers().add(patient);

                } else if (type.equals("admin")) {
                    Administrator admin = new Administrator(id, username, firstname, lastname, password);
                    storage.getUsers().add(admin);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
