package Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DigitalKeyApiRequest {
    String operatorId;
    String assetId;
    String timePeriodStart;
    String timePeriodEnd;
}
