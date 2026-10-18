package th.ac.tu.smartparking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import th.ac.tu.smartparking.entity.ParkingLot;

import java.util.List;

/** DAO for dbo.parking_lot. */
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    /** US02: every lot, sorted by signage code (P1, P2, ... M1, M2). */
    List<ParkingLot> findAllByOrderByCodeAsc();

    /** US04: lots whose code, name or building contains the keyword (ignores upper/lower case). */
    @Query("""
            SELECT p FROM ParkingLot p
            WHERE LOWER(p.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.nameTh) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(p.building) LIKE LOWER(CONCAT('%', :keyword, '%'))
            ORDER BY p.code
            """)
    List<ParkingLot> search(@Param("keyword") String keyword);

    /** US05: lots for one vehicle type that still have free spots. */
    List<ParkingLot> findByVehicleTypeAndAvailableSpotsGreaterThan(String vehicleType, int availableSpots);
}