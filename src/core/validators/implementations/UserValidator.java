/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.validators.implementations;

import core.controllers.responses.Response;

/**
 *
 * @author Victus
 */
public class UserValidator<T> extends Validator<T> {

    // Phone: exactly 10 digits
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("\\d{10}");
    }

    // Email: XXXXX@XXXXX.com
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("[^@]+@[^@]+\\.com");
    }

    @Override
    public Response validate(T object) {
        return null;
    }
}
