package edu.pdx.cs410J.sfabini;

/**
 * Created by sfabini on 7/26/16.
 */
public class Options {
    private boolean hostSpecified = false;
    private String host;
    private boolean portSpecified = false;
    private int port;
    private boolean searchSpecified = false;
    private boolean printSpecified = false;
    private boolean readmeSpecified = false;
    private int count = 0;

    public static Options createOptions(String[] args) {
        Options options = new Options();
        boolean hostNameIsNext = false;
        boolean portNameIsNext = false;
        for (String arg : args) {
            if (arg.equals("-README")) {
                options.setReadmeSpecified(true);
            }
            if (arg.equals("-print")) {
                options.setPrintSpecified(true);
            }
            if (arg.equals("-search")) {
                options.setSearchSpecified(true);
            }
            if (hostNameIsNext) {
                options.setHostSpecified(true, arg);
                hostNameIsNext = false;
            }
            if (arg.equals("-host")) {
                hostNameIsNext = true;
            }
            if (portNameIsNext) {
                options.setPortSpecified(true, arg);
                portNameIsNext = false;
            }
            if (arg.equals("-port")) {
                portNameIsNext = true;
            }

        }
        return options;
    }


    public int getCount() {
        return count;
    }

    public boolean isHostSpecified() {
        return hostSpecified;
    }

    public void setHostSpecified(boolean hostSpecified, String host) {
        if (host == null) {
            throw new IllegalArgumentException("No host specified.");
        }
        this.hostSpecified = hostSpecified;
        this.host = host;
        if (hostSpecified && host != null) {
            count += 2;
        }
    }

    public String getHost() {
        return host;
    }

    public boolean isPortSpecified() {
        return portSpecified;
    }

    public void setPortSpecified(boolean portSpecified, String portString) throws IllegalArgumentException {
        if (portString == null) {
            throw new IllegalArgumentException("No port specified.");
        }
        port = Integer.parseInt(portString);
        this.portSpecified = portSpecified;
        if (portSpecified && port != 0) {
            count += 2;
        }


    }

    public int getPort() {
        return port;
    }


    public boolean isSearchSpecified() {
        return searchSpecified;
    }

    public void setSearchSpecified(boolean searchSpecified) {
        this.searchSpecified = searchSpecified;
        if (searchSpecified) {
            ++count;
        }
    }

    public boolean isPrintSpecified() {
        return printSpecified;
    }

    public void setPrintSpecified(boolean printSpecified) {
        this.printSpecified = printSpecified;
        ++count;
    }

    public boolean isReadmeSpecified() {
        return readmeSpecified;
    }

    public void setReadmeSpecified(boolean readmeSpecified) {
        this.readmeSpecified = readmeSpecified;
        if (readmeSpecified) {
            ++count;
        }

    }



}
