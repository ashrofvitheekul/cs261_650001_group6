package th.ac.tu.smartparking.dto;

/** Body for 4xx answers, e.g. {"message":"ไม่พบลานจอด"}. The frontend shows the message as is. */
public record ErrorResponse(String message) {
}