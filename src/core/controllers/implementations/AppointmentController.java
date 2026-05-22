/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.implementations;
import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.entities.Appointment;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Asaeza07
 */
public class AppointmentController {
     private List<Appointment> appointments;

    public AppointmentController() {

        appointments = new ArrayList<>();
    }

    public Response<Appointment> crearCita(Appointment appointment) {

        appointments.add(appointment);

        return new Response<>(
                StatusCode.SUCCESS,
                "Cita creada correctamente",
                appointment
        );
    }

    public Response<List<Appointment>> obtenerCitas() {

        return new Response<>(
                StatusCode.SUCCESS,
                "Lista de citas obtenida",
                appointments
        );
    }
}
