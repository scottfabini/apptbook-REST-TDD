package edu.pdx.cs410J.sfabini;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parses a file into an <code>AppointmentBook</code>
 */
public class TextParser implements AppointmentBookParser {
    String fileName;
    String appointmentBookOwner;

    /**
     * Initializing constructor specifying the name of the file the Textparser will use
     * @param fileName The file name
     */
    public TextParser(String fileName) {
        this.fileName = fileName;
        if (fileName.contains(".")) {
            this.appointmentBookOwner = fileName.substring(0, fileName.lastIndexOf('.'));
        } else {
            this.appointmentBookOwner = new String(fileName);
        }
    }

    /**
     * Initializing constructor specifying the name of the file the Textparser will use
     * @param fileName The file name
     * @param appointmentBookOwner The owner of the file's appointment book
     */
    public TextParser(String fileName, String appointmentBookOwner) {
        this.fileName = fileName;
        this.appointmentBookOwner = appointmentBookOwner;
    }

    /**
     * Initializing constructor specifying the name of the file the Textparser will use
     * @param file The file
     */
    public TextParser(File file) {
        this.fileName = file.getName();
    }

    /**
     * Parses the file specified in the constructor
     * @return The AppointmentBook derived from the read file
     * @throws ParserException Indicates if Owner doesn't match, or file is malformed
     */
    public AbstractAppointmentBook parse() throws ParserException {
        String ownerLine;
        AbstractAppointmentBook appointmentBook = null;
        BufferedReader br;

        try {
            File file = new File(fileName);
            if (file.exists() == false) {
                file.createNewFile();
            }
            br = new BufferedReader(new FileReader(fileName));
            ownerLine = br.readLine();
            if (ownerLine != null) {
                if (ownerLine.trim().compareTo(appointmentBookOwner) != 0) {
                    throw new ParserException("Owner in file does not match command line argument\n");
                }
                appointmentBook = createAbstractAppointmentBook(ownerLine, br);
            }
        }
        catch (IOException ex) {
            System.err.println(ex);
            throw new ParserException("Unable to read from file: " + fileName);
        }

        return appointmentBook;
    }

    /**
     * creates an appointment book from an owner string and a reader containing other string elements.
     * Refactored out of main body code to make main code cleaner.  Could probably use a little more refactoring.
     * @param ownerLine The owner of the appointment book
     * @param br A buffered reader containing the rest of the initial appointment book information
     * @return The AppointmentBook
     * @throws IOException Occurs if can't create
     * @throws ParserException Occurs if input arguments to parser are malformed
     */
    private AbstractAppointmentBook createAbstractAppointmentBook(String ownerLine, BufferedReader br)
            throws IOException, ParserException {
        AbstractAppointmentBook appointmentBook;
        String appointmentLine;
        appointmentBook = new AppointmentBook(ownerLine);
        while (br.ready()) {
            appointmentLine = br.readLine();
            String [] appointmentLineSplit = appointmentLine.split(" ");
            if (appointmentLineSplit.length != 7) {
                throw new ParserException("Insufficient number of fields for appointment in file");
            }
            AbstractAppointment appointment = new Appointment(appointmentLineSplit[0],
                    appointmentLineSplit[1] + " " + appointmentLineSplit[2] + " " + appointmentLineSplit[3],
                    appointmentLineSplit[4] + " " + appointmentLineSplit[5] + " " + appointmentLineSplit[3]);
            appointmentBook.addAppointment(appointment);
        }
        return appointmentBook;
    }


    /**
     * creates an appointment book from an owner string and a reader containing other string elements.
     * Refactored out of main body code to make main code cleaner.  Could probably use a little more refactoring.
     * @param appointmentBookString the initial appointment book information
     * @return The AppointmentBook
     * @throws IOException Occurs if can't create
     * @throws ParserException Occurs if input arguments to parser are malformed
     */
    private AbstractAppointmentBook createAbstractAppointmentBook(String appointmentBookString)
            throws IOException, ParserException {
        String ownerString;
        String appointmentString;
        AbstractAppointmentBook<AbstractAppointment> appointmentBook;
        AbstractAppointment appointment;
        String description;
        String beginTimeString;
        String endTimeString;

        String [] ownerAndAppointment = appointmentBookString.split(" -> ");
        ownerString = ownerAndAppointment[0];
        appointmentString = ownerAndAppointment[1];
        appointmentBook = createAbstractAppointmentBook(ownerString);
        appointment = createAbstractAppointment(appointmentString);
        appointmentBook.addAppointment(appointment);
        return appointmentBook;
    }


    /**
     * creates an appointment book from an owner string and a reader containing other string elements.
     * Refactored out of main body code to make main code cleaner.  Could probably use a little more refactoring.
     * @param appointmentString the initial appointment book information
     * @return The Appointment
     * @throws IOException Occurs if can't create
     * @throws ParserException Occurs if input arguments to parser are malformed
     */
    private AbstractAppointment createAbstractAppointment(String appointmentString)
            throws IOException, ParserException {
        String [] appointmentLineSplit = appointmentString.split(" ");
        if (appointmentLineSplit.length != 7) {
            throw new ParserException("Insufficient number of fields for appointment in file");
        }
        AbstractAppointment appointment = new Appointment(appointmentLineSplit[0],
                appointmentLineSplit[1] + " " + appointmentLineSplit[2] + " " + appointmentLineSplit[3],
                appointmentLineSplit[4] + " " + appointmentLineSplit[5] + " " + appointmentLineSplit[3]);

        return appointment;
    }

}
