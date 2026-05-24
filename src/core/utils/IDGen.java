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

    private static final Map<String, Integer> appointmentCounters = new HashMap<>();
    private static final Map<String, Integer> hospitalizationCounters = new HashMap<>();

    public static String generateAppointmentId(String patientId) {
        int count = appointmentCounters.getOrDefault(patientId, 0);
        String appointmentId = String.format("A-%s-%04d", patientId, count);
        appointmentCounters.put(patientId, count + 1);
        return appointmentId;
    }

    public static String generateHospitalizationId(String patientId) {
        int count = hospitalizationCounters.getOrDefault(patientId, 0);
        String hospitalizationId = String.format("H-%s-%04d", patientId, count);
        hospitalizationCounters.put(patientId, count + 1);
        return hospitalizationId;
    }

    public static void registerExistingAppointmentId(String appointmentId) {
        String[] parts = appointmentId.split("-");
        if (parts.length != 3) {
            return;
        }
        String patientId = parts[1];
        int sequence = Integer.parseInt(parts[2]);
        appointmentCounters.put(patientId, Math.max(appointmentCounters.getOrDefault(patientId, 0), sequence + 1));
    }

    public static void registerExistingHospitalizationId(String hospitalizationId) {
        String[] parts = hospitalizationId.split("-");
        if (parts.length != 3) {
            return;
        }
        String patientId = parts[1];
        int sequence = Integer.parseInt(parts[2]);
        hospitalizationCounters.put(patientId, Math.max(hospitalizationCounters.getOrDefault(patientId, 0), sequence + 1));
    }
}
