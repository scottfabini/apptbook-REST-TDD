package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * Created by sfabini on 7/21/16.
 */
public class PrettyPrinter implements AppointmentBookDumper
{
    private final PrintWriter writer;

    public PrettyPrinter(PrintWriter pw) {
        this.writer = pw;
    }

    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        int count = 0;
        AppointmentBook book = (AppointmentBook) abstractAppointmentBook;
        if (abstractAppointmentBook != null) {
            for (Appointment appointment : book.getAppointments()) {
                this.writer.print(book.getOwnerName());
                this.writer.print(" -> ");
                this.writer.print(appointment.getDescription());
                this.writer.print(" ");
                this.writer.print(appointment.getBeginTimeString());
                this.writer.print(" ");
                this.writer.print(appointment.getEndTimeString());
                this.writer.print("\n");
                ++count;
            }
        }
        this.writer.flush();
    }



    public int  dumpDateRange(AbstractAppointmentBook abstractAppointmentBook, Date beginTime, Date endTime) throws IOException {
        int count = 0;
        AppointmentBook book = (AppointmentBook) abstractAppointmentBook;
        if (abstractAppointmentBook != null) {
            for (Appointment appointment : book.getAppointments()) {
                if (appointment.getBeginTime().compareTo(beginTime) >= 0
                        && appointment.getEndTime().compareTo(endTime) <= 0)  {
                    ++count;
                    this.writer.print(book.getOwnerName());
                    this.writer.print(" -> ");
                    this.writer.print(appointment.getDescription());
                    this.writer.print(" ");
                    this.writer.print(appointment.getBeginTimeString());
                    this.writer.print(" ");
                    this.writer.print(appointment.getEndTimeString());
                    this.writer.print("\n");
                }

            }
        }
        this.writer.flush();
        return count;
    }
}
