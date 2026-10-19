package th.ac.tu.smartparking.service;

import org.springframework.stereotype.Service;
import th.ac.tu.smartparking.dao.ParkingLotRepository;
import th.ac.tu.smartparking.dto.NearestLotResponse;
import th.ac.tu.smartparking.dto.ParkingLotResponse;
import th.ac.tu.smartparking.entity.ParkingLot;

import java.util.Comparator;
import java.util.List;

/** Business rules for parking lots: list (US02), search (US04), nearest lot of a vehicle type (US05). */
@Service
public class ParkingLotService {

    private static final double EARTH_RADIUS_METERS = 6_371_000;

    private final ParkingLotRepository repository;

    public ParkingLotService(ParkingLotRepository repository) {
    this.repository = repository;
    }

/** US02 + US04: every lot, or only lots matching the keyword when one is given. */
    public List<ParkingLotResponse> findAll(String keyword) {
        List<ParkingLot> lots = (keyword == null || keyword.isBlank())
        ? repository.findAllByOrderByCodeAsc()
        : repository.search(keyword.trim());
        return lots.stream().map(this::toResponse).toList();
    }

    public ParkingLotResponse findById(Long id) {
        return repository.findById(id)
        .map(this::toResponse)
    .orElseThrow(() -> new NotFoundException("ไม่พบลานจอด"));
    }

/** US05: the closest lot of this vehicle type (Sprint 1 does not look at free spots). */
    public NearestLotResponse findNearest(double latitude, double longitude, String vehicleType) {
        String type = normalizeVehicleType(vehicleType);
        return repository.findByVehicleTypeOrderByCodeAsc(type).stream()
        .map(lot -> new NearestLotResponse(toResponse(lot), Math.round(distanceMeters(
        latitude, longitude, lot.getLatitude().doubleValue(), lot.getLongitude().doubleValue()))))
        .min(Comparator.comparingLong(NearestLotResponse::distanceMeters))
        .orElseThrow(() -> new NotFoundException("ยังไม่มีข้อมูลลานจอด"));
    }

/** Straight-line distance between two GPS points (haversine formula). */
    static double distanceMeters(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return 2 * EARTH_RADIUS_METERS * Math.asin(Math.sqrt(a));
    }

    private String normalizeVehicleType(String vehicleType) {
        String type = vehicleType == null ? ParkingLot.CAR : vehicleType.trim().toUpperCase();
        if (!ParkingLot.CAR.equals(type) && !ParkingLot.MOTORCYCLE.equals(type)) {
            throw new IllegalArgumentException("ประเภทรถต้องเป็น CAR หรือ MOTORCYCLE");
        }
        return type;
    }

    private ParkingLotResponse toResponse(ParkingLot lot) {
        return new ParkingLotResponse(
        lot.getId(),
        lot.getCode(),
        lot.getNameTh(),
        lot.getBuilding(),
        lot.getVehicleType(),
        lot.getLatitude(),
        lot.getLongitude());
 }
}