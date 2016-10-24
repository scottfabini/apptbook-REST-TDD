package edu.pdx.cs410J.sfabini;

/**
 * Factory method and accessors for passing and manipulating command line arguments fields
 */
public class Arguments {
    private String owner;
    private String description;
    private String beginTimeString;
    private String endTimeString;
    private int count;

    public static Arguments createArguments(String[] args, Options options) {
        Arguments arguments = new Arguments();
        int ownerIndex;
        int descriptionIndex;
        int beginTimeStringIndex;
        int endTimeStringIndex;

        if (options.isSearchSpecified()) {
            ownerIndex = options.getCount();
            beginTimeStringIndex = ownerIndex + 1;
            endTimeStringIndex = beginTimeStringIndex + 3;
            if (endTimeStringIndex == args.length - 3) {
                arguments.owner = args[ownerIndex];
                arguments.beginTimeString = args[beginTimeStringIndex] + " "
                        + args[beginTimeStringIndex+1] + " "
                        + args[beginTimeStringIndex+2];
                arguments.endTimeString = args[endTimeStringIndex] + " "
                        + args[endTimeStringIndex+1] + " "
                        + args[endTimeStringIndex+2];
            } else { throw new IllegalArgumentException("Owner, begin time, end time required with -search"); }
        } else if (options.isHostSpecified() && options.isPortSpecified()) {
            ownerIndex = options.getCount();
            descriptionIndex = ownerIndex + 1;
            beginTimeStringIndex = descriptionIndex + 1;
            endTimeStringIndex = beginTimeStringIndex + 3;
            if (endTimeStringIndex == args.length - 3) {
                arguments.owner = args[ownerIndex];
                arguments.description = args[descriptionIndex];
                arguments.beginTimeString = args[beginTimeStringIndex] + " "
                        + args[beginTimeStringIndex+1] + " "
                        + args[beginTimeStringIndex+2];
                arguments.endTimeString = args[endTimeStringIndex] + " "
                        + args[endTimeStringIndex+1] + " "
                        + args[endTimeStringIndex+2];
            } else if (ownerIndex < args.length) {
                arguments.owner = args[ownerIndex];

            }
        }
        return arguments;
    }

    /**
     * various getters, setters, and predicates
     */
    public boolean isAppointmentSpecified() {
        if (owner != null && description != null && beginTimeString != null && endTimeString != null)
            return true;
        else return false;
    }

    public boolean isOwner() {
        return owner == null ? false : true;
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
        ++count;
    }
    public boolean isDescription() {
        return description == null ? false : true;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        ++count;
    }

    public boolean isBeginTimeString() {
        return beginTimeString == null ? false : true;
    }

    public String getBeginTimeString() {
        return beginTimeString;
    }

    public void setBeginTimeString(String beginTimeString) {
        this.beginTimeString = beginTimeString;
        ++count;
    }

    public boolean isEndTimeString() {
        return endTimeString == null ? false : true;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public void setEndTimeString(String endTimeString) {
        this.endTimeString = endTimeString;
        ++count;
    }

    public int getCount() {
        return count;
    }


}
