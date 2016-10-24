package edu.pdx.cs410J.sfabini;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "apptbook";
    private static final String SERVLET = "appointments";

    private final String url;


    /**
     * Creates a client to the appointment book REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AppointmentBookRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Returns all keys and values from the server
     */
    public Response getAllKeysAndValues() throws IOException
    {
        return get(this.url );
    }

    /**
     * Returns all values for the given key
     */
    public Response getValues( String owner ) throws IOException
    {
        return get(this.url, "owner", owner);
    }


    /**
     * Returns range of values for the given key
     */
    public Response getValues( String owner, String beginTime, String endTime ) throws IOException
    {
        return get(this.url, "owner", owner, "beginTime", beginTime, "endTime", endTime);
    }

    public Response addOwnerAppointmentPair( String owner, String description, String beginTime, String endTime ) throws IOException
    {
        return postToMyURL("owner", owner, "description", description, "beginTime", beginTime, "endTime", endTime);
    }

    @VisibleForTesting
    Response postToMyURL(String... keysAndValues) throws IOException {
        return post(this.url, keysAndValues);
    }

    public Response removeAllMappings() throws IOException {
        return delete(this.url);
    }





    public Response createAppointment(String owner, String description, String beginTime, String endTime) throws IOException {
        return post(this.url, "owner", owner, "description", description, "beginTime", beginTime, "endTime", endTime);
    }


}
