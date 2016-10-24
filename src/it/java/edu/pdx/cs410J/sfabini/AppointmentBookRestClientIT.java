package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.web.HttpRequestHelper.Response;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  @Ignore
  public void invokingGetWithPrettyPrintOwnerParameterPrettyPrintsOwnerName() throws IOException {
    String owner = "PreCannedOwner";
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    Response response = client.getValues(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));

  }

  @Ignore
  public void invokingPOSTCreatesAnAppointment() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "PreCannedOwner";
    String description = "My description";
    String beginTime = "01/01/1970 12:00 am";
    String endTime = "07/22/2016 12:00 am";
    Response response = client.createAppointment(owner, description, beginTime, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.getValues(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));
    assertThat(response.getContent(), containsString(description));
    assertThat(response.getContent(), containsString(beginTime));
    assertThat(response.getContent(), containsString(endTime));
  }

  @Test
  public void invokingMultiplePOSTsCreatesMultipleAppointments() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "PreCannedOwner";
    String description = "My description";
    String beginTime = "01/01/1970 12:00 am";
    String endTime = "07/22/2016 12:00 am";
    Response response = client.createAppointment(owner, description, beginTime, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.getValues(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));
    assertThat(response.getContent(), containsString(description));
    assertThat(response.getContent(), containsString(beginTime));
    assertThat(response.getContent(), containsString(endTime));


    owner = "Second Owner";
    description = "Second description";
    beginTime = "01/01/1980 12:00 am";
    endTime = "07/22/2000 12:00 am";
    response = client.createAppointment(owner, description, beginTime, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.getValues(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));
    assertThat(response.getContent(), containsString(description));
    assertThat(response.getContent(), containsString(beginTime));
    assertThat(response.getContent(), containsString(endTime));
  }


  @Test
  public void invokingGetOnEmptyAppointmentBook() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = null;
    String description = null;
    String beginTime = null;
    String endTime = null;

    Response response = client.removeAllMappings();
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.getAllKeysAndValues();
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString("Server contains 0 appointment"));

  }

  @Test
  public void testGetValues() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "Test Owner";
    String description = "Second description";
    String beginTime = "01/01/1980 12:00 am";
    String endTime = "07/22/2000 12:00 am";

    Response response = client.removeAllMappings();
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.addOwnerAppointmentPair(owner, description, beginTime, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.getValues(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString("Server contains 1 appointment"));
    assertThat(response.getContent(), containsString("Test Owner"));

  }

  @Test
  public void getAllKeysAndValues() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner1 = "Test Owner 1";
    String description1 = "First description";
    String beginTime1 = "01/01/1980 12:00 am";
    String endTime1 = "07/22/2000 12:00 am";

    String owner2 = "Test Owner 2";
    String description2 = "Second description";
    String beginTime2 = "01/01/1990 12:00 am";
    String endTime2 = "07/22/2010 12:00 am";

    Response response = client.removeAllMappings();
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.addOwnerAppointmentPair(owner1, description1, beginTime1, endTime1);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    response = client.addOwnerAppointmentPair(owner2, description2, beginTime2, endTime2);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.getAllKeysAndValues();
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString("Server contains 2 appointment"));
    assertThat(response.getContent(), containsString("Test Owner 1"));
    assertThat(response.getContent(), containsString("Test Owner 2"));

  }



}
