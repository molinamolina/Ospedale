/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.implementations;

import core.controllers.interfaces.IAuthController;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Victus
 */
public class AuthController implements IAuthController {

    private final List<HashMap<String, String>> users;

    public AuthController(List<HashMap<String, String>> users) {
        this.users = users;
    }

    @Override
    public Response login(String username, String password) {
        for (HashMap<String, String> user : users) {
            if (user.get("username").equals(username) && user.get("password").equals(password)) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("username", username);
                return new Response(
                        "Successfully logged in.",
                        StatusCode.OK,
                        data
                );
            }
        }
        return new Response(
                "Bad or nonexistant credentials",
                StatusCode.BAD_REQUEST
        );
    }
}
