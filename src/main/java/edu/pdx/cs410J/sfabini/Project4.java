package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {

    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String... args) {

        Appointment appointment = null;
        Options options;
        Arguments arguments;


        if (args.length == 0) {
            System.err.println(MISSING_ARGS);
            System.exit(1);
        }
        options = Options.createOptions(args);
        arguments = Arguments.createArguments(args, options);


        if (options.isReadmeSpecified()) {
            usage("-README");
            System.exit(0);
        }

        if (arguments.isAppointmentSpecified()) {
            appointment = new Appointment(arguments.getDescription(),
                    arguments.getBeginTimeString(), arguments.getEndTimeString());
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(options.getHost(), options.getPort());

        HttpRequestHelper.Response response = null;
        try {
            if (arguments.isOwner() == false) {
                // Print all owner/value pairs
                response = client.getAllKeysAndValues();

            } else if (options.isSearchSpecified() && arguments.isOwner() && arguments.isBeginTimeString() && arguments.isEndTimeString()) {
                // Search the response by date.
                response = client.getValues(arguments.getOwner(), arguments.getBeginTimeString(), arguments.getEndTimeString());
            }
            else if (arguments.isOwner() == true && arguments.isAppointmentSpecified() == false) {
                // Print all values of key
                response = client.getValues(arguments.getOwner());

            } else if (arguments.isOwner() == true && arguments.isAppointmentSpecified() == true){
                // Post the key/value pair
                response = client.addOwnerAppointmentPair(arguments.getOwner(), appointment.getDescription(), appointment.getBeginTimeString()
                        , appointment.getEndTimeString());
            }

            if (response != null) {
                checkResponseCode( HttpURLConnection.HTTP_OK, response);
                System.out.println(response.getContent());
            }

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        detectPrintOption(args, appointment);
        System.exit(0);
    }





    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }


    /**
     * Detects if the -print option was specified as a command-line argument.
     * If so, parses the AppointmentBook information from the file.
     * @param args The command-line arguments
     */
    protected static void detectPrintOption(String [] args, Appointment appointment) {
        if (appointment == null) {
            return;
        }
        for (String arg : args) {
            if (arg.compareTo("-print") == 0) {
                System.out.println("\n-print option of most recent appointment: ");
                System.out.println(appointment);
            }
        }
    }
    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("Scott Fabini Project 4");
        err.println("This simple program posts owner-Appointment pairs to the server");
        err.println("If no Appointment is specified, then all Appointments for that owner are printed");
        err.println("If no key is specified, all owner/Appointment pairs are printed");
        err.println();

        System.exit(1);
    }
}