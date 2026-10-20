package th.ac.tu.smartparking.dto;

/** US05 ลานที่ใกล้สุด + ระยะทาง (เมตร) */
public record NearestLotResponse(ParkingLotResponse lot, long distanceMeters) {
}