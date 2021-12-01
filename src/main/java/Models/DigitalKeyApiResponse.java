package Models;

import lombok.Data;

@Data
public class DigitalKeyApiResponse {
    String id;
    String operatorId;
    String assetId;
    String timePeriodStart;
    String timePeriodEnd;
}
