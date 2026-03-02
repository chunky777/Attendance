package com.example.demo;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("/attendance")
public class AttendanceController {
    
    private final AttendanceRepository repository;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(AttendanceRepository repository,EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/clockin")
    public String clockIn(@RequestParam String employeeCode){

        Optional<Employee> optionalEmployee = employeeRepository.findByEmployeeCode(employeeCode);

        if(optionalEmployee.isEmpty()) {
            return "社員コードが見つかりません";
        }
        
        Employee employee = optionalEmployee.get();
        Attendance attendance = new Attendance();
        attendance.setEmployee(employee);
        attendance.setClockIn(LocalDateTime.now());

        repository.save(attendance);

        return "出勤完了";
    }
    
    @GetMapping("/clockout")
    public String clockOut(@RequestParam String name){
        Optional<Attendance> optional = repository.findTopByEmployeeNameAndClockOutIsNullOrderByClockInDesc(name);

        if(optional.isEmpty()) {
            return "出勤データがありません";
        }

        Attendance attendance = optional.get();
        attendance.setClockOut(LocalDateTime.now());


        repository.save(attendance);

        return "退勤完了";
    }
}
