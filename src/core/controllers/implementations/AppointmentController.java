/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.implementations;

import core.controllers.responses.Response;
import core.controllers.responses.StatusCode;
import core.models.entities.Appointment;
import core.controllers.interfaces.IAppointmentController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Asaeza07
 */
public class AppointmentController implements IAppointmentController {

    private List<Appointment> appointments;

    public AppointmentController() {
        appointments = new ArrayList<>();
    }

    @Override
    public Response crearCita(Appointment appointment) {
        appointments.add(appointment);

        HashMap<String, Object> data = new HashMap<>();
        data.put("appointment", appointment);

        return new Response(
                "Cita creada correctamente",
                StatusCode.CREATED,
                data
        );
    }

    @Override
    public Response obtenerCitas() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("appointments", appointments);

        return new Response(
                "Lista de citas obtenida",
                StatusCode.OK,
                data
        );
    }
}
