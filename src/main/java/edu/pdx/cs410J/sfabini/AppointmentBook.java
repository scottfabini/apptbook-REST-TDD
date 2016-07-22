package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.Collection;

/**
 * Created by sfabini on 7/21/16.
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
    private final String owner ;

    public AppointmentBook(String owner) {
        this.owner = owner;
    }

    @Override
    public String getOwnerName() {
        return this.owner;
    }

    @Override
    public Collection<Appointment> getAppointments() {
        return null;
    }

    @Override
    public void addAppointment(Appointment appt) {

    }


}
