# Integration Tests: UI ↔ API

| รหัส | ทดสอบ | วิธีทำ (F12 → Network) | ต้องได้ |
|---|---|---|---|---|
| IT-UI-1 | โหลดลาน | เปิดหน้าแรก | `GET /api/parking-lots`200 · ได้ 8 ลาน |
| IT-UI-2 | ค้นหา | พิมพ์ "หอสมุด" ในช่องค้นหา | `GET /api/parking-lots?q=...`200 · ได้ P3 |
| IT-UI-3 | ใกล้สุด | ตั้ง Sensors `14.0687, 100.6056` → รถยนต์ → กดหาลาน | `GET /api/parking-lots/nearest?...&type=CAR`200 · ได้ P2 |
| IT-UI-4 | API ล่ม | `docker compose stop backend` แล้วกด F5 | หน้าเว็บขึ้นข้อความแจ้งข้อผิดพลาด ไม่ค้าง |
| IT-UI-5 | CORS | เปิดหน้าเว็บจาก http://localhost:3000 แล้วดู Console | ไม่มี CORS error ใน Console |

