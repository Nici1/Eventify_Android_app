package fe.uni.lj.si.eventify;

import android.graphics.Bitmap;

public class Event {
    private int venueAvailabilityID;
    private int performerID;
    private String performerName;
    private String performerDescription;
    private String performerLink;
    private String venueID;
    private String venueName;
    private String venueDescription;
    private Bitmap venueImage;
    private Bitmap performerImage;
    private String datesAndTimes;

    public Event(int venueAvailabilityID, int performerID, String performerName, String performerDescription,
                 String performerLink, String venueID, String venueName, String venueDescription,
                 Bitmap venueImage, String datesAndTimes, Bitmap performerImage) {
        this.venueAvailabilityID = venueAvailabilityID;
        this.performerID = performerID;
        this.performerName = performerName;
        this.performerDescription = performerDescription;
        this.performerLink = performerLink;
        this.venueID = venueID;
        this.venueName = venueName;
        this.venueDescription = venueDescription;
        this.venueImage = venueImage;
        this.performerImage = performerImage;
        this.datesAndTimes = datesAndTimes;
    }

    // Getters
    public int getVenueAvailabilityID() { return venueAvailabilityID; }
    public int getPerformerID() { return performerID; }
    public String getPerformerName() { return performerName; }
    public String getPerformerDescription() { return performerDescription; }
    public String getPerformerLink() { return performerLink; }
    public String getVenueID() { return venueID; }
    public String getVenueName() { return venueName; }
    public String getVenueDescription() { return venueDescription; }
    public Bitmap getVenueImage() { return venueImage; }
    public Bitmap getPerformerImage() { return performerImage; }
    public String getDatesAndTimes() { return datesAndTimes; }
}
