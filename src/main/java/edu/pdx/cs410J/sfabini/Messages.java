package edu.pdx.cs410J.sfabini;

import com.sun.corba.se.impl.logging.IORSystemException;
import sun.nio.ch.IOStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String getMappingCount( int count )
    {
        return String.format( "Server contains %d appointments", count );
    }

    public static String formatAppointmentBook( String owner, AppointmentBook appointmentBook ) throws IOException {
        if (appointmentBook == null) {
            return "Server contains 0 appointments";
        }
        TextDumper td = new TextDumper();
        return td.dumpToString(appointmentBook);
    }

    public static String missingRequiredParameter( String parameterName )
    {
        return String.format("The required parameter \"%s\" is missing", parameterName);
    }

    public static String mappedAppointment( String owner, AppointmentBook appointmentBook) throws IOException
    {

        return String.format( "Mapped %s to appointment book %s", owner,
                formatAppointmentBook(owner, appointmentBook) );
    }

    public static String allMappingsDeleted() {
        return "All mappings have been deleted";
    }
}
