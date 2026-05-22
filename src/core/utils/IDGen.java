/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Victus
 */
public class IDGen {

    private static final Map<String, Integer> counters = new HashMap<>();

    public static String IDGen(String patientID) {
        int count = counters.getOrDefault(patientID, 0);
        String appointmentID = String.format("A-%s-%04d", patientID, count);
        counters.put(patientID, count + 1);
        return appointmentID;
    }
}
