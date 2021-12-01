package Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Equipment {
    String icNumber;
    String hercEquipmentName;
    String hercCustomName;
    String rmStartDate;
    String rmEndDate;

}
