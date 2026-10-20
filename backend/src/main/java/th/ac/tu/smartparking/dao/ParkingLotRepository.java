package th.ac.tu.smartparking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import th.ac.tu.smartparking.entity.ParkingLot;

import java.util.List;

/** DAO ของตาราง dbo.parking_lot */
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    /** US02 ลานทั้งหมด เรียงตามรหัส */
    List<ParkingLot> findAllByOrderByCodeAsc();

    /**
     * US04 ค้นจากรหัส ชื่อลาน หรืออาคาร
     * กัน % _ [ ไม่ให้เป็น wildcard ของ LIKE
     */
    default List<ParkingLot> search(String keyword) {
        String plain = keyword.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_")
                .replace("[", "\\[");
        return searchLike(plain);
    }

    @Query("""
            SELECT p FROM ParkingLot p
            WHERE LOWER(p.code) LIKE LOWER(CONCAT('%', :keyword, '%')) ESCAPE '\\'
               OR LOWER(p.nameTh) LIKE LOWER(CONCAT('%', :keyword, '%')) ESCAPE '\\'
               OR LOWER(p.building) LIKE LOWER(CONCAT('%', :keyword, '%')) ESCAPE '\\'
            ORDER BY p.code
            """)
    List<ParkingLot> searchLike(@Param("keyword") String keyword);

    /** US05 ลานของรถประเภทนั้น (service เป็นคนหาลานใกล้สุด) */
    List<ParkingLot> findByVehicleTypeOrderByCodeAsc(String vehicleType);
}