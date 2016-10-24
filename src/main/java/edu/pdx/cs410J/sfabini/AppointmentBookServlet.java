package edu.pdx.cs410J.sfabini;

import com.google.common.annotations.VisibleForTesting;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet
{

    private final Map<String, AppointmentBook> appointmentBooks = new ConcurrentHashMap<>();
    int appointmentCountOnServer;


    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );



        String owner = getParameter("owner", request);
        String description = getParameter("description", request);
        String beginTimeString = getParameter("beginTime", request);
        String endTimeString = getParameter("endTime", request);


        String key = getParameter("key", request);
        String value = getParameter("value", request);
        // Use key/values for web interface
        if (key != null && value != null) {
            String[] split = value.split(" ");
            if (split.length == 7) {
                description = split[0];
                beginTimeString = split[1] + " " + split[2] + " " + split[3];
                endTimeString = split[4] + " " + split[5] + " " + split[6];
            }
            if (split.length == 6) {
                beginTimeString = split[1] + " " + split[2] + " " + split[3];
                endTimeString = split[4] + " " + split[5] + " " + split[6];
            }
            owner = key;
        }

        if (owner == null && beginTimeString == null && endTimeString == null) {
            writeAllMappings(response);
        }
        else if (owner != null && description == null && beginTimeString != null && endTimeString != null) {
            createResponseWithRangeOfAppointments(response, owner, beginTimeString, endTimeString);
        }
        else if (owner != null && beginTimeString == null && endTimeString == null) {
            writeValue(owner, response);

        }
        else {
            response.getWriter().append(Messages.getMappingCount(appointmentCountOnServer));
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }



    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        String key = getParameter("key", request);
        String value = getParameter("value", request);
        String owner = getParameter("owner", request);
        String description = getParameter("description", request);
        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);



        // Use key/values for web interface
        if (key != null && value != null) {
            owner = key;
            String[] split = value.split(" ");
            if (split.length == 7) {
                description = split[0];
                beginTime = split[1] + " " + split[2] + " " + split[3];
                endTime = split[4] + " " + split[5] + " " + split[6];

            }
        }

        if (owner == null) {
            missingRequiredParameter(response, "post requires owner or key");
        }
        if (description == null || beginTime == null || endTime == null) {
            missingRequiredParameter(response, "post requires all appointment parameters");
        }

        AppointmentBook book = createSingletonAppointmentBookForOwner(owner);


        if (description != null && beginTime != null && endTime != null) {
            Appointment appointment = new Appointment(description, beginTime, endTime);
            book.addAppointment(appointment);
            appointmentCountOnServer++;
        }
        appointmentBooks.put(owner, book);
        response.getWriter().append(Messages.mappedAppointment(owner, book));
        response.getWriter().append(Messages.getMappingCount(appointmentCountOnServer));
        response.getWriter().flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles an HTTP DELETE request by removing all key/value pairs.  This
     * behavior is exposed for testing purposes only.  It's probably not
     * something that you'd want a real application to expose.
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");

        this.appointmentBooks.clear();
        appointmentCountOnServer = 0;

        PrintWriter pw = response.getWriter();
        pw.println(Messages.allMappingsDeleted());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);

    }

    /**
     * Called from doGet when owner != null && beginTimeString != null && endTimeString != null
     * @param response The http response message
     * @param owner The owner of the appointment book
     * @param beginTimeString The beginTime as string.  The appointments will be in the range beginTime-endTime
     * @param endTimeString The endTime as string
     * @throws IOException
     */
    @VisibleForTesting
    protected void createResponseWithRangeOfAppointments(HttpServletResponse response,
                                                       String owner, String beginTimeString, String endTimeString) throws IOException {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        Date beginTime = null;
        Date endTime = null;
        try {
            beginTime = df.parse(beginTimeString);
            endTime = df.parse(endTimeString);
            prettyPrint(this.appointmentBooks.get(owner), beginTime, endTime, response.getWriter());
            response.getWriter().append(Messages.getMappingCount(appointmentCountOnServer));
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



    /**
     * Writes the value of the given key to the HTTP response.
     *
     * The text of the message is formatted with {@link Messages#getMappingCount(int)}
     * and {@link Messages#formatAppointmentBook(String, AppointmentBook)} (String, Appointment)}
     */
    @VisibleForTesting
    private void writeValue( String owner, HttpServletResponse response ) throws IOException
    {
        AppointmentBook appointmentBook = this.appointmentBooks.get(owner);

        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount( appointmentBook != null ? 1 : 0 ));
        pw.println(Messages.formatAppointmentBook(owner, appointmentBook));

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Writes all of the key/value pairs to the HTTP response.
     * Called from doGet when owner == null && beginTimeString == null && endTimeString == null
     *
     * The text of the message is formatted with
     * {@link Messages#formatAppointmentBook(String, AppointmentBook)}
     */
    private void writeAllMappings( HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount(appointmentBooks.size()));

        for (Map.Entry<String, AppointmentBook> entry : this.appointmentBooks.entrySet()) {
            pw.println(Messages.formatAppointmentBook(entry.getKey(), entry.getValue()));
        }

        pw.flush();

        response.setStatus( HttpServletResponse.SC_OK );
    }




    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
            throws IOException
    {

        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    public AppointmentBookServlet() {
        createPreCannedAppointmentBooks();
    }

    private void createPreCannedAppointmentBooks() {
        String owner = "PreCannedOwner";
        AppointmentBook book = new AppointmentBook(owner);
        this.appointmentBooks.put(owner, book);
    }


    private void prettyPrint(AppointmentBook book, PrintWriter pw) throws IOException {
        PrettyPrinter pretty = new PrettyPrinter(pw);
        pretty.dump(book);
    }

    private void prettyPrint(AppointmentBook book, Date beginTime, Date endTime, PrintWriter pw) throws IOException {
        PrettyPrinter pretty = new PrettyPrinter(pw);
        pretty.dumpDateRange(book, beginTime, endTime);
    }

    @VisibleForTesting
    protected AppointmentBook createSingletonAppointmentBookForOwner(String owner) {
        AppointmentBook book = this.appointmentBooks.get(owner);
        if (book == null) {
            book = new AppointmentBook(owner);
            this.appointmentBooks.put(owner, book);
        }
        return book;
    }

}
