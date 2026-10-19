# System Tests

ทดสอบทั้งระบบบนเครื่องที่ clone ใหม่ เดินตาม sequence diagram

| รหัส | ทดสอบ | ขั้นตอน | ต้องได้ |
|---|---|---|---|
| SYS-1 | ติดตั้งจากศูนย์ | clone ใหม่ → `docker compose up -d --build` | 3 container running ภายใน 10 นาที · `/api/health` = UP |
| SYS-2 | Sequence US02 | เปิดหน้าแรก | หมุด 8 จุด ข้อมูลตรงกับตาราง `parking_lot` |
| SYS-3 | Sequence US04 | ค้นหา "หอสมุด" → ล้างคำค้น | ค้นหาได้ P3 → ล้างแล้วกลับเป็น 8 ลาน |
| SYS-4 | Sequence US05 | Sensors `14.0687, 100.6056`  → เลือกรถยนต์ → หาลาน → นำทาง | P2 ห่างประมาณ 150 ม. → เปิด Google Maps |
| SYS-5 | Sequence US05 (ไม่ให้ตำแหน่ง) | Sensors → Location unavailable → หาลาน | ข้อความให้เลือกลานจากรายการ |
| SYS-6 | Sequence US02 (ไม่มีข้อมูล) | รัน `tc02-empty.sql` → F5 → คืนข้อมูลด้วย down -v + up | "ยังไม่มีข้อมูลลานจอด" → กลับมา 8 ลาน |
| SYS-7 | ข้อมูลอยู่ต่อหลังปิดเครื่อง | `docker compose down` → `docker compose up -d` | ข้อมูลยังอยู่ครบ |
| SYS-8 | มือถือ | F12 → จอ 360 px → เดินซ้ำ SYS-2 ถึง SYS-4 | ใช้งานได้ครบ |
