package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = System.getProperty("http.port", "8080");

    @Test
    public void test0RemoveAllMappings() throws IOException {
      AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
      HttpRequestHelper.Response response = client.removeAllMappings();
      assertThat(response.getContent(), response.getCode(), equalTo(200));

    }

    @Test
    public void test1NoCommandLineArguments() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString(Project4.MISSING_ARGS));
    }

    @Test
    public void test2EmptyServer() {
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT );
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        assertThat(out, out, containsString(Messages.getMappingCount(0)));
    }

    @Test
    public void test3NoValues() throws IOException {
        String owner = "TestOwner";
        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT, owner );
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        //sdassertThat(out, out, containsString(Messages.getMappingCount(0)));
        assertThat(out, out, containsString(Messages.formatAppointmentBook(owner, null)));
    }

    @Test
    public void test4AddValues() throws IOException {
        String owner = "Test Owner 4";
        String description = "TestDescription";
        String beginTimeStringDate = "01/01/1970";
        String beginTimeStringTime = "12:00";
        String beginTimeStringAmPm = "am";
        String endTimeStringDate = "01/01/1970";
        String endTimeStringTime = "12:00";
        String endTimeStringAmPm = "am";
        String beginTimeString = concatenateDateString(beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm);
        String endTimeString = concatenateDateString(endTimeStringDate, endTimeStringTime, endTimeStringAmPm);


        String description2 = "TestDescription2";
        String beginTimeStringDate2 = "01/01/1970";
        String beginTimeStringTime2 = "12:00";
        String beginTimeStringAmPm2 = "am";
        String endTimeStringDate2 = "01/01/1970";
        String endTimeStringTime2 = "12:00";
        String endTimeStringAmPm2 = "am";
        String beginTimeString2 = concatenateDateString(beginTimeStringDate2, beginTimeStringTime2, beginTimeStringAmPm2);
        String endTimeString2 = concatenateDateString(endTimeStringDate2, endTimeStringTime2, endTimeStringAmPm2);

        String description3 = "TestDescription3";
        String beginTimeStringDate3 = "01/01/1970";
        String beginTimeStringTime3 = "12:00";
        String beginTimeStringAmPm3 = "am";
        String endTimeStringDate3 = "01/01/1970";
        String endTimeStringTime3 = "12:00";
        String endTimeStringAmPm3 = "am";
        String beginTimeString3 = concatenateDateString(beginTimeStringDate3, beginTimeStringTime3, beginTimeStringAmPm3);
        String endTimeString3 = concatenateDateString(endTimeStringDate3, endTimeStringTime3, endTimeStringAmPm3);


        MainMethodResult result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner, description, beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm,
                endTimeStringDate, endTimeStringTime, endTimeStringAmPm);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();
        Appointment appointment = new Appointment(description, beginTimeString, endTimeString);
        AppointmentBook appointmentBook = new AppointmentBook(owner);
        appointmentBook.addAppointment(appointment);
        assertThat(out, out, containsString(Messages.mappedAppointment(owner, appointmentBook)));

        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner, description2, beginTimeStringDate2, beginTimeStringTime2, beginTimeStringAmPm2,
                endTimeStringDate2, endTimeStringTime2, endTimeStringAmPm2);
        out = result.getOut();
        appointment = new Appointment(description2, beginTimeString2, endTimeString2);
        appointmentBook.addAppointment(appointment);
        assertThat(out, out, containsString(Messages.getMappingCount(2)));
        assertThat(out, out, containsString("TestDescription2"));

        result = invokeMain( Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner, description3, beginTimeStringDate3, beginTimeStringTime3, beginTimeStringAmPm3,
                endTimeStringDate3, endTimeStringTime3, endTimeStringAmPm3);
        out = result.getOut();
        appointment = new Appointment(description3, beginTimeString3, endTimeString3);
        appointmentBook.addAppointment(appointment);
        assertThat(out, out, containsString(Messages.getMappingCount(3)));
        assertThat(out, out, containsString("TestDescription3"));
    }

    @Test
    public void test5GETWithJustOwnerNamePrettyPrintsAll() throws IOException {
        String owner = "Test Owner 4";


        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();

        assertThat(out, out, containsString("Test Owner 4"));
        assertThat(out, out, containsString("TestDescription"));
        assertThat(out, out, containsString("TestDescription2"));
        assertThat(out, out, containsString("TestDescription3"));
    }

    @Test
    public void test6PostWithNewOwnerCreatesAppointmentBook() throws IOException {
        String owner = "Test Owner 5";

        String description = "TestDescription5";
        String beginTimeStringDate = "01/01/1970";
        String beginTimeStringTime = "12:00";
        String beginTimeStringAmPm = "am";
        String endTimeStringDate = "01/01/1970";
        String endTimeStringTime = "12:00";
        String endTimeStringAmPm = "am";
        String beginTimeString = concatenateDateString(beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm);
        String endTimeString = concatenateDateString(endTimeStringDate, endTimeStringTime, endTimeStringAmPm);



        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner, description, beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm,
                endTimeStringDate, endTimeStringTime, endTimeStringAmPm);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();

        assertThat(out, out, containsString("Test Owner 5"));
        assertThat(out, out, containsString("TestDescription5"));

    }

    @Test
    public void test7GetSingleOwnerAppointmentBook() throws IOException {
        String owner = "Test Owner 5";

        String description = "TestDescription5";
        String beginTimeStringDate = "01/01/1970";
        String beginTimeStringTime = "12:00";
        String beginTimeStringAmPm = "am";
        String endTimeStringDate = "01/01/1970";
        String endTimeStringTime = "12:00";
        String endTimeStringAmPm = "am";
        String beginTimeString = concatenateDateString(beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm);
        String endTimeString = concatenateDateString(endTimeStringDate, endTimeStringTime, endTimeStringAmPm);



        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner, description, beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm,
                endTimeStringDate, endTimeStringTime, endTimeStringAmPm);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));



        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();

        assertThat(out, out, containsString("Test Owner 5"));
        assertThat(out, out, containsString("TestDescription5"));

        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                "Dave", "1/1/1970", "12:00", "am", "12/25/1999", "12:00", "am");

        out = result.getOut();
        assertThat(out, out, containsString("Dave"));


        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        out = result.getOut();

        assertThat(out, out, containsString("Dave"));
    }

    @Test
    public void test8Search() throws IOException {
        String owner = "Test Owner 5";

        String description = "TestDescription5";
        String beginTimeStringDate = "01/02/1970";
        String beginTimeStringTime = "12:00";
        String beginTimeStringAmPm = "am";
        String endTimeStringDate = "01/03/1970";
        String endTimeStringTime = "12:00";
        String endTimeStringAmPm = "am";
        String beginTimeString = concatenateDateString(beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm);
        String endTimeString = concatenateDateString(endTimeStringDate, endTimeStringTime, endTimeStringAmPm);



        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                owner, description, beginTimeStringDate, beginTimeStringTime, beginTimeStringAmPm,
                endTimeStringDate, endTimeStringTime, endTimeStringAmPm);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));



        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT,
                "TestDescription5", "Description2", "1/1/1985", "12:00", "am", "12/25/1986", "12:00", "am");

       result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "-search", "TestDescription5", "1/1/1985", "12:00", "am", "12/25/1986", "12:00", "am");


        result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, owner);
        assertThat(result.getErr(), result.getExitCode(), equalTo(0));
        String out = result.getOut();

        assertThat(out, out, containsString("Dave"));
    }

    private String concatenateDateString(String date, String time, String amPm) {
        return date + " " + time + " " + amPm;
    }
}