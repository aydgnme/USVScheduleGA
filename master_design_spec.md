# Master Design Specification

## Overview
USV Schedule GA is an automated scheduling system for university courses using Genetic Algorithms. The system provides role-based access for Admins, Secretaries, Teachers, and Students.

## Roles & Responsibilities

### Admin
- **Manage Base Data**: Faculties, Departments, Teachers, Rooms, Groups.
- **Control Scheduling**: Trigger Genetic Algorithm generation.
- **System Oversight**: View all schedules and logs.

### Secretary
- **Department Management**: Manage courses and assignments for their specific department.
- **Manual Adjustments**: Tweaking the generated schedule for specific groups/slots.
- **Course Assignments**: Linking Teachers to Course Offerings.

### Teacher
- **Availability**: Set preferred/unavailable timeslots.
- **Viewer**: View personal schedule.

### Student
- **Public View**: View group or year schedules without login.

## Design Principles
- **Clean UI**: Vaadin-based, responsive design.
- **Role Isolation**: Strict view access control based on Spring Security roles.
- **Algorithm Integration**: Seamless trigger and result visualization for the GA.

## Technical Components
- **Backend**: Spring Boot 3.5.x, Java 21, SQLite.
- **Frontend**: Vaadin Flow 24.
- **Algorithm**: Custom Genetic Algorithm engine (to be implemented/integrated).
