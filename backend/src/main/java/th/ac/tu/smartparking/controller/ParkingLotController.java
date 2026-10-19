package th.ac.tu.smartparking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import th.ac.tu.smartparking.dto.ErrorResponse;
import th.ac.tu.smartparking.dto.NearestLotResponse;
import th.ac.tu.smartparking.dto.ParkingLotResponse;
import th.ac.tu.smartparking.service.NotFoundException;
import th.ac.tu.smartparking.service.ParkingLotService;

import java.util.List;

/**
 * GET /api/parking-lots every lot (US02), ?q=keyword to search (US04)
 * GET /api/parking-lots/{id} one lot
 * GET /api/parking-lots/nearest closest lot of one vehicle type (US05)
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/parking-lots")
public class ParkingLotController {

 private final ParkingLotService parkingLotService;

 public ParkingLotController(ParkingLotService parkingLotService) {
 this.parkingLotService = parkingLotService;
 }

 @GetMapping
 public List<ParkingLotResponse> findAll(@RequestParam(name = "q", required = false) String keyword) {
 return parkingLotService.findAll(keyword);
 }

 @GetMapping("/nearest")
 public NearestLotResponse findNearest(
 @RequestParam double lat,
 @RequestParam double lng,
 @RequestParam(defaultValue = "CAR") String type) {
 return parkingLotService.findNearest(lat, lng, type);
 }

 @GetMapping("/{id}")
 public ParkingLotResponse findById(@PathVariable Long id) {
 return parkingLotService.findById(id);
 }

 @ExceptionHandler(NotFoundException.class)
 @ResponseStatus(HttpStatus.NOT_FOUND)
 public ErrorResponse notFound(NotFoundException e) {
 return new ErrorResponse(e.getMessage());
 }

 @ExceptionHandler(IllegalArgumentException.class)
 @ResponseStatus(HttpStatus.BAD_REQUEST)
 public ErrorResponse badRequest(IllegalArgumentException e) {
 return new ErrorResponse(e.getMessage());
 }
}