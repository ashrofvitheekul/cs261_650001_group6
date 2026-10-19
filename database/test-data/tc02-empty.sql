-- US02 test case 4: no parking lot data -> "ยังไม่มีข้อมูลลานจอด"
-- Restore afterwards: docker compose down -v && docker compose up -d --build
DELETE FROM dbo.parking_lot;
GO
