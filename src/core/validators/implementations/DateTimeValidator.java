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
public class DateTimeValidator<T> extends Validator<T> {

    // Date: YYYY-MM-DD
    public static boolean isValidDate(String date) {
        if (date == null) return false;
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) return false;
        String[] parts = date.split("-");
        int month = Integer.parseInt(parts[1]);
        int day   = Integer.parseInt(parts[2]);
        return month >= 1 && month <= 12 && day >= 1 && day <= 31;
    }

    // Time 24h with quarter minutes: hh:mm where mm ∈ {00,15,30,45}
    public static boolean isValidTime(String time) {
        if (time == null) return false;
        if (!time.matches("\\d{2}:\\d{2}")) return false;
        String[] parts = time.split(":");
        int hour   = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);
        return hour >= 0 && hour <= 23
            && (minute == 0 || minute == 15 || minute == 30 || minute == 45);
    }

    @Override
    public Response validate(T object) {
        return null;
    }
}
