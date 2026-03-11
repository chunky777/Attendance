package com.example.demo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceRepository repository;
    private final EmployeeRepository employeeRepository;

    public AttendanceController(AttendanceRepository repository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping("/clockin")
    public String clockIn() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

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
    public String clockOut(Authentication authentication) {

        String employeeCode = authentication.getName();

        Employee employee = employeeRepository
                .findByEmployeeCode(employeeCode)
                .orElseThrow();

        Optional<Attendance> optional = repository.findTopByEmployeeAndClockOutIsNullOrderByClockInDesc(employee);

        if (optional.isEmpty()) {
            return "出勤データがありません";
        }

        Attendance attendance = optional.get();

        if (attendance.getClockOut() != null) {
            return "すでに退勤済みです";
        }

        attendance.setClockOut(LocalDateTime.now());

        repository.save(attendance);

        return "退勤完了";
    }

    // CSV出力
    @Autowired
    private AttendanceService attendanceService;

   @GetMapping("/export")
public void exportCsv(HttpServletResponse response) throws IOException {

    String filename = "attendance.csv";

    response.setContentType("text/csv; charset=UTF-8");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

    PrintWriter writer = response.getWriter();

    // Excel 文字化け対策（BOM）
    writer.write('\uFEFF');

    // ヘッダー
    writer.println("社員コード,名前,出勤日時,退勤日時");

    // 勤怠データ取得
    List<Attendance> list = attendanceService.findAll();

    for (Attendance a : list) {
        writer.println(
            a.getEmployee().getEmployeeCode() + "," +
            a.getEmployee().getName() + "," +
            a.getClockIn() + "," +
            a.getClockOut()
        );
    }

    writer.flush();
    }
}
