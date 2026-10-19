package th.ac.tu.smartparking.dto;

/** US05: the closest lot of the chosen vehicle type, and how far it is in a straight line. */
public record NearestLotResponse(ParkingLotResponse lot, long distanceMeters) {
}