package th.ac.tu.smartparking.dto;

import java.math.BigDecimal;

public record ParkingLotResponse(
 Long id,
 String code,
 String nameTh,
 String building,
 String vehicleType,
 BigDecimal latitude,
 BigDecimal longitude,
 String zone) {
}