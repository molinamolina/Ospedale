package core.utils;

import core.models.entities.Administrator;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.models.entities.User;
import core.repositories.DataStorage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class JSONLoader {

    public static void userLoad() {
        DataStorage storage = DataStorage.getInstance();
        try {
            Path jsonPath = resolveJsonPath();
            String content = Files.readString(jsonPath, StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(content);
            JSONArray userArray = root.getJSONArray("users");

            for (int i = 0; i < userArray.length(); i++) {
                JSONObject jsonUser = userArray.getJSONObject(i);
                String type = jsonUser.getString("type");
                long id = jsonUser.getLong("id");
                String username = jsonUser.getString("username");
                String firstname = jsonUser.getString("firstname");
                String lastname = jsonUser.getString("lastname");
                String password = jsonUser.getString("password");

                switch (type) {
                    case "admin" -> storage.getUsers().add(new Administrator(id, username, firstname, lastname, password));
                    case "patient" -> storage.getUsers().add(new Patient(
                            id,
                            username,
                            firstname,
                            lastname,
                            password,
                            jsonUser.getString("email"),
                            LocalDate.parse(jsonUser.getString("birthdate")),
                            jsonUser.getBoolean("gender"),
                            jsonUser.getLong("phone"),
                            jsonUser.getString("address")
                    ));
                    case "doctor" -> storage.getUsers().add(new Doctor(
                            id,
                            username,
                            firstname,
                            lastname,
                            password,
                            SpecialtyMapper.fromJson(jsonUser.getString("specialty")),
                            jsonUser.getString("licenceNumber"),
                            jsonUser.getString("assignedOffice")
                    ));
                    default -> {
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static Path resolveJsonPath() {
        Path direct = Paths.get("json/users.json");
        if (Files.exists(direct)) {
            return direct;
        }
        Path fromBuild = Paths.get("../json/users.json");
        if (Files.exists(fromBuild)) {
            return fromBuild;
        }
        return direct;
    }
}
