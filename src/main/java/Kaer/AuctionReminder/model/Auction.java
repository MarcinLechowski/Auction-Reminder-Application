package Kaer.AuctionReminder.model;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class Auction {
    private String id;
    private String title;
    private String endTime;

    public Auction(String id, String title, String endTime) {
        this.id = id;
        this.title = title;
        this.endTime = endTime;
    }
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getEndTime() {
        return endTime;
    }
}