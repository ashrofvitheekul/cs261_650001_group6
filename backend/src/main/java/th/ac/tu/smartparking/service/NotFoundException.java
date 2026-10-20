package th.ac.tu.smartparking.service;

/** ไม่พบข้อมูล -> controller ตอบ 404 */
public class NotFoundException extends RuntimeException {

 public NotFoundException(String message) {
 super(message);
 }
}