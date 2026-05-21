/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

/**
 *
 * @author Victus
 */
public class DateTimeManager {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Set<Integer> VALID_MINUTES = Set.of(0, 15, 30, 45);

    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    public static boolean isValidTime(String time) {
        try {
            LocalTime parsed = LocalTime.parse(time, TIME_FORMATTER);
            return VALID_MINUTES.contains(parsed.getMinute());
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}