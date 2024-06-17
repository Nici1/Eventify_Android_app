package fe.uni.lj.si.eventify;

import java.util.List;

public class EventResponse {
    private List<Event> result;
    private List<String> venue_images;
    private List<String> performer_images;

    // Getters and setters

    public List<Event> getResult() {
        return result;
    }

    public void setResult(List<Event> result) {
        this.result = result;
    }

    public List<String> getVenue_images() {
        return venue_images;
    }

    public void setVenue_images(List<String> venue_images) {
        this.venue_images = venue_images;
    }

    public List<String> getPerformer_images() {
        return performer_images;
    }

    public void setPerformer_images(List<String> performer_images) {
        this.performer_images = performer_images;
    }

    public static class Event {
        private int VenueID;
        private String VenueName;
        private String Description;
        // Add other fields as needed

        // Getters and setters
        public int getVenueID() {
            return VenueID;
        }

        public void setVenueID(int venueID) {
            VenueID = venueID;
        }

        public String getVenueName() {
            return VenueName;
        }

        public void setVenueName(String venueName) {
            VenueName = venueName;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }
    }
}
