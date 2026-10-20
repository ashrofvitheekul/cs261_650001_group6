# TU Smart Parking - CS261 กลุ่ม 6, Section 650001

เว็บดูลานจอดรถในมหาวิทยาลัยธรรมศาสตร์ ศูนย์รังสิต: ดูลานทั้งหมดบนแผนที่ (US02), ค้นหาลานจากรหัส ชื่อลาน หรือชื่ออาคาร (US04), หาลานที่ใกล้ที่สุดตามประเภทรถแล้วนำทางด้วย Google Maps (US05)

## ลิงก์

| | |
|---|---|
| Trello | https://trello.com/w/group6_section650001 |
| Figma (UI prototype) | https://www.figma.com/design/SwhNM99Ktq5LBWnrn9ugWk |
| Release | ดูแท็บ Releases ของ repo นี้ (`v1.0` = Sprint 1) |

## วิธีรัน (ต้องมี Docker Desktop)

```bash
git clone <ลิงก์ repo นี้>
cd cs261_650001_group6
docker compose up -d --build
```

รอประมาณ 2–5 นาที แล้วเปิด http://localhost:3000

```bash
docker compose ps
```

ต้องเห็น `sp-db` (healthy), `sp-backend` (healthy), `sp-frontend` (running), `sp-db-init` เป็น `Exited (0)` คือปกติ (รัน SQL เสร็จแล้วปิดตัว)

ปิดระบบ: `docker compose down` (ข้อมูลยังอยู่), ล้างข้อมูลแล้วเริ่มใหม่: `docker compose down -v`

## พอร์ต

| Service | URL | หมายเหตุ |
|---|---|---|
| frontend (Node/Express) | http://localhost:3000 | หน้าเว็บ |
| backend (Spring Boot บน Tomcat) | http://localhost:8081/api/health | ต้องได้ `{"api":"UP","database":"UP"}` |
| db (MS SQL Server 2019) | localhost:1433 | ผู้ใช้ `sa`, เปลี่ยนพอร์ตได้ด้วย `DB_PORT` |

## API

| Method | Path | ใช้กับ |
|---|---|---|
| GET | `/api/health` | เช็กว่า API และฐานข้อมูลพร้อม |
| GET | `/api/parking-lots` | US02 ลานทั้งหมด |
| GET | `/api/parking-lots?q=หอสมุด` | US04 ค้นหา |
| GET | `/api/parking-lots/{id}` | ลานเดียว |
| GET | `/api/parking-lots/nearest?lat=14.0687&lng=100.6056&type=CAR` | US05 ลานใกล้สุด (`CAR` หรือ `MOTORCYCLE`) |

Postman: `docs/postman/sprint1.postman_collection.json`

## แก้ปัญหาที่เจอบ่อย

| อาการ | วิธีแก้ |
|---|---|
| หน้าเว็บไม่ขึ้น 8 ลาน / ข้อมูลเป็นของเก่า | `docker compose down -v` แล้ว `docker compose up -d --build` |
| พอร์ต 1433 ถูกใช้อยู่ (เครื่องมี SQL Server) | PowerShell: `$env:DB_PORT=14330; docker compose up -d`, Mac/Git Bash: `DB_PORT=14330 docker compose up -d` |
| `sp-db` ขึ้น `sqlservr.sfp invalid` | `docker compose up -d` อีกครั้ง, ถ้ายังไม่ได้ `wsl --shutdown` แล้วเปิด Docker Desktop ใหม่ |
| แผนที่ไม่ขึ้น | ต้องต่ออินเทอร์เน็ต (แผนที่ OpenStreetMap, Leaflet, ฟอนต์ โหลดจากเน็ต) |

## โฟลเดอร์

| โฟลเดอร์ | มีอะไร |
|---|---|
| `backend/` | Spring Boot (Entity, DAO, Service, Controller) + unit test |
| `frontend/` | หน้าเว็บ HTML/CSS/JS + Leaflet |
| `database/` | SQL สร้างตาราง + ข้อมูล 8 ลาน (รันอัตโนมัติตามลำดับเลขไฟล์), `test-data/` สคริปต์ทดสอบ |
| `docs/design/` | ER, class, sequence, deployment diagram, รูป UI prototype |
| `docs/test-specs/` | test case ทุกระดับ |
| `docs/test-reports/` | ผลทดสอบ + รูปหลักฐาน |

## ทีม

1. Scrum Master (1 คน)
   คุม Sprint, Daily Scrum / Review / Retro, Trello, Docker, รวมโค้ด, release, ส่ง Moodle
   งาน: Task6, Task7, Task8, Task9, Task18 (คู่ภาณุพงศ์), Task22 (คู่วชิรวิทย์), Task23, Task24, Task25
* อัชรอฟ

2. ฐานข้อมูล (1 คน)
   ER diagram, SQL ตาราง + ข้อมูลตัวอย่าง, test spec ส่วน DAO
   งาน: Task5, Task10
* พิชญาภา

3. Backend (2 คน)
   คนที่ 1: Entity + DAO + DAO test + Sequence diagram + Integration test API ↔ DAO (Task4, Task11, Task20)
   คนที่ 2: API spec + DTO + Service + Controller + unit test + Postman + รวมโค้ด (Task3, Task12, Task18)
* แทนไท
* ภาณุพงศ์

4. Frontend (2 คน)
   คนที่ 1: หน้าแผนที่ + รายการลาน US02 + Integration test UI ↔ API (Task13, Task19)
   คนที่ 2: UI prototype + ค้นหา US04 + หาลานใกล้สุด US05 (Task2, Task14, Task15)
* สุจิรา
* ณัฏฐนิชา

5. QA / Tester (1 คน)
   ทบทวน User Story Detail, test spec, สคริปต์ข้อมูลทดสอบ, system test, UAT, รายงานผล
   งาน: Task1, Task16, Task17, Task21, Task22
* วชิรวิทย์
