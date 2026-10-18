package th.ac.tu.smartparking.dto;

import java.math.BigDecimal;

/** One parking lot as the frontend sees it (US02 map, US04 search, US05 nearest lot). */
public record ParkingLotResponse(
 Long id,
 String code,
 String nameTh,
 String building,
 String vehicleType,
 BigDecimal latitude,
 BigDecimal longitude,
 int totalSpots,
 int availableSpots) {
}