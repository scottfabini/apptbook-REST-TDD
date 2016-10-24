package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Dumps text from an <code>AppointmentBook</code> to a file
 */
public class TextDumper implements AppointmentBookDumper {
    String fileName;

    public TextDumper() {
        fileName = null;
    }
    /**
     * Initializing constructor with String argument
     * @param fileName The file name to which to dump
     */
    public TextDumper(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Initializing constructor with File argument
     * @param file The file to which to dump
     */
    public TextDumper(File file) {
        this.fileName = file.getName();
    }

    /**
     * Dumps an appointmentBook to a file
     * @param appointmentBook The appointmentBook to dump
     * @throws IOException Propagate from failed createNewFile or append
     */
    public void dump(AbstractAppointmentBook appointmentBook) throws IOException {
        try {
            File file = new File(fileName);
            file.createNewFile();
            FileWriter writer = new FileWriter(fileName);
            ArrayList<AbstractAppointment> appointments = (ArrayList<AbstractAppointment>) appointmentBook.getAppointments();
            writer.append(appointmentBook.getOwnerName() + "\n");
            for (AbstractAppointment appointment : appointments) {
                writer.append(appointment.getDescription() + " ");
                writer.append(appointment.getBeginTimeString() + " ");
                writer.append(appointment.getEndTimeString() + " \n");
            }
            writer.close();

        } catch (IOException ex) {
            throw ex;
        }
    }
    /**
     * Dumps an appointmentBook to a string
     * @param appointmentBook The appointmentBook to dump
     * @throws IOException Propagate from failed createNewFile or append
     */
    public String dumpToString(AppointmentBook appointmentBook)  {
        StringBuilder sb = new StringBuilder();
        Collection<Appointment> appointments = appointmentBook.getAppointments();
        for (AbstractAppointment appointment : appointments) {
            sb.append(appointmentBook.getOwnerName()).append(" -> ");
            sb.append(dumpToString(appointment));

        }
        System.out.println(sb.toString());
        return sb.toString();
    }

    /**
     * Dumps an appointmentBook to a string
     * @param appointment The appointmentBook to dump
     * @throws IOException Propagate from failed createNewFile or append
     */
    public String dumpToString(AbstractAppointment appointment)  {
        StringBuilder sb = new StringBuilder();
        sb.append(appointment.getDescription()).append(" ");
        sb.append(appointment.getBeginTimeString()).append(" ");
        sb.append(appointment.getEndTimeString()).append("\n");
        return sb.toString();
    }
}
