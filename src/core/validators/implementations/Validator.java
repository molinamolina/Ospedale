/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.validators.implementations;

import core.controllers.responses.Response;
import core.validators.interfaces.IValidator;

/**
 *
 * @author Victus
 */
public abstract class Validator<T> implements IValidator<T> {

    // Validación de número de identifiación.
    // Debe tener exactamente 12 números y ser mayor que 0
    public static boolean isValidId(String id) {
        if (id == null) return false;
        return id.matches("\\d{12}") && !id.equals("000000000000");
    }

    /* Verificar que las dos contraseñas (original y verificación) coincidan.
    Asímismo, asegurar de que ninguna o ambas estén vacías.
    */
    public static boolean passwordsMatch(String password, String confirm) {
        if (password == null || password.isBlank()) return false;
        return password.equals(confirm);
    }

    @Override
    public abstract Response validate(T object);
}
