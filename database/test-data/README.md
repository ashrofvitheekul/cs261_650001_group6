# Test data (ไม่รันอัตโนมัติ)

ใช้กับ test case US02 ข้อ 4 "ยังไม่มีข้อมูลลานจอด" (UI-US02-4, UAT-04, SYS-6, IT-API-5), Sprint 1 ไม่มีเรื่องจำนวนที่ว่างแล้ว จึงเหลือสคริปต์เดียว

### Git Bash

ต้องมี `MSYS_NO_PATHCONV=1` ไม่งั้น Git Bash จะแปลง `/opt/...` เป็น `C:/Program Files/Git/opt/...` แล้ว error

```bash
MSYS_NO_PATHCONV=1 docker exec -i sp-db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "SmartParking@2026" -C -d SmartParking < database/test-data/tc02-empty.sql
```

### PowerShell

```powershell
Get-Content database/test-data/tc02-empty.sql -Encoding UTF8 | docker exec -i sp-db /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "SmartParking@2026" -C -d SmartParking
```

### คืนข้อมูล 8 ลาน

```bash
docker compose down -v
docker compose up -d --build
```