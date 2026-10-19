package th.ac.tu.smartparking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Maps to table dbo.parking_lot (database/02-schema.sql).
 */
@Entity
@Table(name = "parking_lot")
public class ParkingLot {

    public static final String CAR = "CAR";
    public static final String MOTORCYCLE = "MOTORCYCLE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "name_th", nullable = false, length = 150)
    private String nameTh;

    @Column(length = 150)
    private String building;

    @Column(name = "vehicle_type", nullable = false, length = 12)
    private String vehicleType;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "total_spots", nullable = false)
    private int totalSpots;

    @Column(name = "available_spots", nullable = false)
    private int availableSpots;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** Lot outline as JSON [[lat,lng],...]; drawn as the lot zone on the map. */
    @Column(name = "zone")
    private String zone;

    protected ParkingLot() {
        // for JPA
    }

    /** Used by unit tests to build lots without a database. */
    public ParkingLot(String code, String nameTh, String building, String vehicleType,
            double latitude, double longitude, int totalSpots, int availableSpots, LocalDateTime updatedAt) {
        this.code = code;
        this.nameTh = nameTh;
        this.building = building;
        this.vehicleType = vehicleType;
        this.latitude = BigDecimal.valueOf(latitude);
        this.longitude = BigDecimal.valueOf(longitude);
        this.totalSpots = totalSpots;
        this.availableSpots = availableSpots;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getNameTh() { return nameTh; }
    public String getBuilding() { return building; }
    public String getVehicleType() { return vehicleType; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public int getTotalSpots() { return totalSpots; }
    public int getAvailableSpots() { return availableSpots; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public String getZone() { return zone; }
}