package Models;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FrontendResponse {
    String accountNumber;
    String assignmentName;
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    Date assignmentStartDate;
//    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    Date assignmentEndDate;
    String adminEmail;
    List<String> operatorEmails;
    List<Equipment> equipments;
}
