package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A book of appointments
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {
    private final String ownerName ;
    private ArrayList<Appointment> appointments;

    /**
     * Creates a new <code>AppointmentBook</code>
     * @param owner The name of the owner
     */
    public AppointmentBook(String owner) {
        this.ownerName = owner;
        this.appointments = new ArrayList<>();
    }

    /**
     * Creates a new <code>AppointmentBook</code> from an ownerName and an initial appointment
     * @param ownerName The name of the owner of the appointment book
     * @param appointment The initial appointment
     */
    public AppointmentBook(String ownerName, Appointment appointment) {
        this.ownerName = ownerName;
        this.appointments = new ArrayList<>();
        this.appointments.add(appointment);
    }

    /**
     * Returns the owner of this <code>AppointmentBook</code>
     */
    @Override
    public String getOwnerName() {
        return this.ownerName;
    }

    /**
     * Returns the Collection of appointments.
     * @return the Collection
     */
    @Override
    public Collection<Appointment> getAppointments() {
        return this.appointments;
    }

    /**
     * Add an appointment to the appointment book.
     *
     * @param appointment The appointment to add
     */
    @Override
    public void addAppointment(Appointment appointment) {
        if (appointment == null) {
            return;
        }
        this.appointments.add(appointment);
    }


    /**
     * Returns a <code>String</code> that describes this
     * <code>AppointmentBook</code>
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getOwnerName())
                .append("\'s appointment book with ")
                .append(this.getAppointments().size())
                .append(" appointment(s)\n");
        for (Appointment appointment : appointments) {
            sb.append(appointment.getDescription()).append(" ");
            sb.append(appointment.getBeginTimeString()).append(" ");
            sb.append(appointment.getEndTimeString()).append(" \n");
        }

        return sb.toString().trim();
    }
}
