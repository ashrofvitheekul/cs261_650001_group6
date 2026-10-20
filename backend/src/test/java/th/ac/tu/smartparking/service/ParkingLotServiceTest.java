package th.ac.tu.smartparking.service;

import org.junit.jupiter.api.Test;
import th.ac.tu.smartparking.dao.ParkingLotRepository;
import th.ac.tu.smartparking.dto.NearestLotResponse;
import th.ac.tu.smartparking.dto.ParkingLotResponse;
import th.ac.tu.smartparking.entity.ParkingLot;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** test ParkingLotService ข้อมูลตาม User Story Detail */
class ParkingLotServiceTest {

 private final ParkingLotRepository repository = mock(ParkingLotRepository.class);
 private final ParkingLotService service = new ParkingLotService(repository);

 private static ParkingLot lot(String code, String type, double lat, double lng, int total, int free) {
 return new ParkingLot(code, "ลาน " + code, "อาคาร " + code, type, lat, lng, total, free, LocalDateTime.now());
 }

 // US02

 @Test
 void noKeywordReturnsEveryLot() {
 when(repository.findAllByOrderByCodeAsc()).thenReturn(List.of(
 lot("M1", "MOTORCYCLE", 14.0718, 100.6048, 260, 74),
 lot("P1", "CAR", 14.07145, 100.6042, 180, 42)));

 List<ParkingLotResponse> lots = service.findAll(null);

 assertEquals(2, lots.size());
 assertEquals("P1", lots.get(1).code());
 }

 @Test
 void blankKeywordAlsoReturnsEveryLot() {
 service.findAll(" ");

 verify(repository).findAllByOrderByCodeAsc();
 }

 // US04

 @Test
 void searchUsesKeywordWithoutSpaces() {
 when(repository.search("หอสมุด")).thenReturn(List.of(lot("P3", "CAR", 14.0687, 100.6029, 320, 118)));

 List<ParkingLotResponse> result = service.findAll(" หอสมุด ");

 assertEquals(1, result.size());
 assertEquals("P3", result.get(0).code());
 }

 // US05

 @Test
 void nearestPicksClosestLot() {
 when(repository.findByVehicleTypeOrderByCodeAsc("CAR")).thenReturn(List.of(
 lot("P3", "CAR", 14.068700, 100.602900, 320, 118),
 lot("P2", "CAR", 14.070200, 100.606550, 240, 15)));

 NearestLotResponse result = service.findNearest(14.066600, 100.607600, "car");

 assertEquals("P2", result.lot().code());
 assertEquals(416, result.distanceMeters(), 5);
 }

 @Test
 void nearestUsesOnlyLotsOfTheChosenVehicleType() {
 when(repository.findByVehicleTypeOrderByCodeAsc("MOTORCYCLE")).thenReturn(List.of(
 lot("M2", "MOTORCYCLE", 14.065400, 100.610100, 300, 25)));

 NearestLotResponse result = service.findNearest(14.066600, 100.607600, "MOTORCYCLE");

 assertEquals("M2", result.lot().code());
 assertEquals(301, result.distanceMeters(), 5);
 }

 @Test
 void nearestThrowsWhenNoLotOfThatType() {
 when(repository.findByVehicleTypeOrderByCodeAsc("CAR")).thenReturn(List.of());

 NotFoundException e = assertThrows(NotFoundException.class,
 () -> service.findNearest(14.0666, 100.6076, "CAR"));

 assertEquals("ยังไม่มีข้อมูลลานจอด", e.getMessage());
 }

 @Test
 void nearestRejectsUnknownVehicleType() {
 assertThrows(IllegalArgumentException.class, () -> service.findNearest(14.0666, 100.6076, "BUS"));
 }

 @Test
 void findByIdThrowsWhenLotDoesNotExist() {
 when(repository.findById(99L)).thenReturn(Optional.empty());

 assertThrows(NotFoundException.class, () -> service.findById(99L));
 }
}