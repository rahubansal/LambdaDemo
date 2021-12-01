package Models;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FrontendResponse {
    String accountNumber;
    String assignmentName;
    String assignmentStartDate;
    String assignmentEndDate;
    String adminEmail;
    List<String> operatorEmails;
    List<Equipment> equipments;
}
