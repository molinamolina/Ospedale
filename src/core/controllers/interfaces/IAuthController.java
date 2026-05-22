/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.interfaces;

import core.controllers.responses.Response;

/**
 *
 * @author Victus
 */
public interface IAuthController {

    Response login(String username, String password);
}
