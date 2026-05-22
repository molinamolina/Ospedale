/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.interfaces;

import core.controllers.responses.Response;
import core.models.entities.Appointment;

/**
 *
 * @author Victus
 */
public interface IAppointmentController {

    Response crearCita(Appointment appointment);

    Response obtenerCitas();
}
