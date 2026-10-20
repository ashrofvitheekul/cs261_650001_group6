package th.ac.tu.smartparking.dto;

/** body ตอน error เช่น {"message":"ไม่พบลานจอด"} */
public record ErrorResponse(String message) {
}