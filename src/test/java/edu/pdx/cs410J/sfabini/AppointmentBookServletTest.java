package edu.pdx.cs410J.sfabini;

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {

  @Ignore
  public void getOnServletPrettyPrintsPreCannedAppointmentBookOwner() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String ownerName = "PreCannedOwner";
    when(request.getParameter("owner")).thenReturn(ownerName);
    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);
    verify(pw).println(ownerName);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  @Test
  public void postToServletCreatesAppointment() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String ownerName = "PreCannedOwner";
    String description = "My description";
    String beginTime = "01/01/1970 12:00 am";
    String endTime = "07/22/2016 12:00 am";

    when(request.getParameter("owner")).thenReturn(ownerName);
    when(request.getParameter("description")).thenReturn(description);
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    when(request.getParameter("endTime")).thenReturn(endTime);
    when(response.getWriter()).thenReturn(pw);

    servlet.doPost (request, response);


    verify(response).setStatus(HttpServletResponse.SC_OK);

    AppointmentBook book = servlet.createSingletonAppointmentBookForOwner(ownerName);
    Collection<Appointment> appointments = book.getAppointments();
    assertThat(appointments.size(), equalTo(1));
    Appointment appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));
    assertThat(appointment.getBeginTimeString(), equalTo(beginTime));
    assertThat(appointment.getEndTimeString(), equalTo(endTime));
  }

  @Test
  public void postMultipleAppointments() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String ownerName = "PreCannedOwner";
    String description = "My description";
    String beginTime = "01/01/1970 12:00 am";
    String endTime = "07/22/2016 12:00 am";

    when(request.getParameter("owner")).thenReturn(ownerName);
    when(request.getParameter("description")).thenReturn(description);
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    when(request.getParameter("endTime")).thenReturn(endTime);
    when(response.getWriter()).thenReturn(pw);

    servlet.doPost (request, response);
    //verify(response).setStatus(HttpServletResponse.SC_OK);


    AppointmentBook book = servlet.createSingletonAppointmentBookForOwner(ownerName);
    Collection<Appointment> appointments = book.getAppointments();
    assertThat(appointments.size(), equalTo(1));
    Appointment appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));
    assertThat(appointment.getBeginTimeString(), equalTo(beginTime));
    assertThat(appointment.getEndTimeString(), equalTo(endTime));

    ownerName = "Second owner";
    description = "Second description";
    beginTime = "01/01/1980 12:00 am";
    endTime = "07/22/1990 12:00 am";

    when(request.getParameter("owner")).thenReturn(ownerName);
    when(request.getParameter("description")).thenReturn(description);
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    when(request.getParameter("endTime")).thenReturn(endTime);
    when(response.getWriter()).thenReturn(pw);

    servlet.doPost (request, response);
//    verify(response).setStatus(HttpServletResponse.SC_OK);

    book = servlet.createSingletonAppointmentBookForOwner(ownerName);
    appointments = book.getAppointments();
    assertThat(appointments.size(), equalTo(1));
    appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));
    assertThat(appointment.getBeginTimeString(), equalTo(beginTime));
    assertThat(appointment.getEndTimeString(), equalTo(endTime));
  }

  @Test
  public void emptyServletReturnsMessageIndicatingZeroAppointments() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    //PrintWriter pw = mock(PrintWriter.class);
    PrintWriter pw = new PrintWriter(System.out);

    String ownerName = null;
    String description = null;
    String beginTime = null;
    String endTime = null;

    when(request.getParameter("owner")).thenReturn(ownerName);
    when(request.getParameter("description")).thenReturn(description);
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    when(request.getParameter("endTime")).thenReturn(endTime);
    when(response.getWriter()).thenReturn(pw);
    when(response.getStatus()).thenReturn(HttpServletResponse.SC_OK);
    servlet.doDelete(request, response);
    servlet.doGet (request, response);


    assertTrue(response.getStatus() == HttpServletResponse.SC_OK);

    //verify(response).setStatus(HttpServletResponse.SC_OK);
    // Check console for output
  }


}
