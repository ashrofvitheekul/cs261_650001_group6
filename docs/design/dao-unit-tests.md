# DAO Unit Test Report — พิชญาภา

## Overview
This document summarizes the unit test cases and results for the Data Access Object (DAO) layer corresponding to US05 (Vehicle Type & Parking Lot Schema).

## Test Environment
- **Database:** MS SQL Server (via Docker Container `sp-db`)
- **Framework:** JUnit 5 / Spring Boot Test
- **Execution Status:** PASSED

## Test Cases

| Test Case ID | Description | Expected Result | Status |
| :--- | :--- | :--- | :--- |
| **DAO-1** | Read all parking lots ordered by code (`findAllByOrderByCodeAsc()`) | Returns 8 parking lots, starting with M1 | **PASS** |
| **DAO-2** | Search parking lots by building name (`search("วิทยาศาสตร์")`) | Returns M1, P1 | **PASS** |
| **DAO-3** | Case-insensitive search (`search("sc")`) | Returns P2 | **PASS** |
| **DAO-4** | Search parking lots by code (`search("P4")`) | Returns P4 | **PASS** |
| **DAO-5** | Search non-existent location (`search("ตึกโดม")`) | Returns empty list | **PASS** |
| **DAO-6** | Find available parking lots filtered by vehicle type (`findByVehicleTypeAndAvailableSpotsGreaterThan("CAR", 0)`) | Returns available CAR lots (excluding P4 with 0 spots) | **PASS** |

## Summary
All DAO unit tests passed successfully without errors.