package com.example.demo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    
    private final AttendanceRepository repository;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(AttendanceRepository repository,EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

@PostMapping("/clockin")
public String clockIn() {

    Authentication auth =
        SecurityContextHolder.getContext().getAuthentication();

    String employeeCode = auth.getName();

    Employee employee = employeeRepository
            .findByEmployeeCode(employeeCode)
            .orElseThrow();

    Attendance attendance = new Attendance();
    attendance.setEmployee(employee);
    attendance.setClockIn(LocalDateTime.now());

    repository.save(attendance);

    return "出勤しました";
}
    
@PostMapping("/clockout")
public String clockOut(Authentication authentication){

    String employeeCode = authentication.getName();

    Employee employee = employeeRepository
            .findByEmployeeCode(employeeCode)
            .orElseThrow();

    Optional<Attendance> optional =
        repository.findTopByEmployeeAndClockOutIsNullOrderByClockInDesc(employee);

    if(optional.isEmpty()) {
        return "出勤データがありません";
    }

    Attendance attendance = optional.get();

    if(attendance.getClockOut() != null){
        return "すでに退勤済みです";
    }

    attendance.setClockOut(LocalDateTime.now());

    repository.save(attendance);

    return "退勤完了";
}
}
