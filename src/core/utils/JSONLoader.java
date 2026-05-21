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

public class JSONLoader {

    public static void userLoad() {

        DataStorage storage = DataStorage.getInstance();
        try {
            String content = new String(Files.readAllBytes(Paths.get("json/users.json")), StandardCharsets.UTF_8);
            JSONObject root = new JSONObject(content);
            JSONArray userArray = root.getJSONArray("users");
            
            // FILL THIS OUT

        } catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
