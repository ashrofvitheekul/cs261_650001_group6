package th.ac.tu.smartparking.dao;

import com.example.demo.DemoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import th.ac.tu.smartparking.entity.ParkingLot;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * test DAO กับ MS SQL จริง (ข้อมูล seed 8 ลาน)
 * ชื่อลงท้าย IT ตอน build จะไม่รัน ต้องสั่งเอง:
 *   docker compose up -d db db-init
 *   cd backend && ./mvnw test -Dtest=ParkingLotRepositoryIT
 */
@SpringBootTest(classes = DemoApplication.class)
class ParkingLotRepositoryIT {

    @Autowired
    private ParkingLotRepository repository;

    @Test
    void findsAllEightSeedLotsSortedByCode() {
        List<ParkingLot> lots = repository.findAllByOrderByCodeAsc();

        assertEquals(8, lots.size());
        assertEquals("M1", lots.get(0).getCode());
    }

    @Test
    void searchMatchesBuildingName() {
        List<ParkingLot> lots = repository.search("วิทยาศาสตร์");

        assertEquals(List.of("M1", "P1"), lots.stream().map(ParkingLot::getCode).toList());
    }

    @Test
    void searchIgnoresUpperAndLowerCase() {
        List<ParkingLot> lots = repository.search("sc");

        assertEquals("P2", lots.get(0).getCode());
    }

    @Test
    void searchByCode() {
        assertEquals("P4", repository.search("P4").get(0).getCode());
    }

    @Test
    void searchReturnsNothingForUnknownPlace() {
        assertTrue(repository.search("ตึกโดม").isEmpty());
    }

    @Test
    void percentAndUnderscoreAreNotWildcards() {
        assertTrue(repository.search("%").isEmpty());
        assertTrue(repository.search("_").isEmpty());
        assertTrue(repository.search("[").isEmpty());
    }

    @Test
    void onlyLotsOfThatVehicleType() {
        List<ParkingLot> cars = repository.findByVehicleTypeOrderByCodeAsc(ParkingLot.CAR);

        assertEquals(List.of("P1", "P2", "P3", "P4", "P5", "P6"), cars.stream().map(ParkingLot::getCode).toList());
        assertTrue(cars.stream().allMatch(l -> l.getVehicleType().equals("CAR")));
    }
}