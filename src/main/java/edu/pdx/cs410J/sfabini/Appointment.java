package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents an <code>Appointment</code>
 */
public class Appointment extends AbstractAppointment implements Comparable {
    private String description;
    private Date beginTime;
    private Date endTime;

    /**
     * Creates a new <code>Appointment</code>
     *
     * @param description The appointment description
     * @param beginTimeString The time the appointment begins
     * @param endTimeString   The time the appointment ends
     */
    public Appointment(String description, String beginTimeString, String endTimeString) {
        this.description = description;
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
            beginTime = df.parse(beginTimeString);
            endTime = df.parse(endTimeString);
        } catch (ParseException ex) {
            System.err.println("Malformatted date detected.\n" + ex.getMessage());
        }
    }

    /**
     * Several getters/setters
     */
    @Override
    public String getBeginTimeString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        return df.format(beginTime).toLowerCase();
    }

    @Override
    public String getEndTimeString() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        return df.format(endTime).toLowerCase();
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.getDescription() + " from " + this.getBeginTimeString() + " until " + this.getEndTimeString();
    }

    /**
     * Override the compareTo of Comparable interface.  Makes a series of comparisons: earliest beginTime,
     * earliestEndTime, and earliest lexigraphical description.
     * @param o The object to compare to.  This class only performs comparisons to objects of the same class.
     * @return 0 if equal. negative number if less than, per the assignment criteria (beginTime first...).
     * positive number if greater than.
     */
    @Override
    public int compareTo(Object o) {
        int comparisonResult = 0;
        if (this.getClass().equals(o.getClass())) {
            Appointment appointment = (Appointment) o;
            comparisonResult = this.beginTime.compareTo(appointment.beginTime);
            if (comparisonResult == 0) {
                comparisonResult = this.endTime.compareTo(appointment.endTime);
                if (comparisonResult == 0) {
                    comparisonResult = this.description.compareTo(appointment.description);
                }
            }
        }
        return comparisonResult;
    }
}
