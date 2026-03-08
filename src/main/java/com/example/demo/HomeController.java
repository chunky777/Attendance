package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {

    private final EmployeeRepository employeeRepository;

    public HomeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

@GetMapping("/attendance")
public String attendancePage(Authentication authentication, Model model){

    String employeeCode = authentication.getName();

    Employee employee = employeeRepository.findByEmployeeCode(employeeCode).orElseThrow();
    model.addAttribute("employeeName", employee.getName());
    
    return "attendance";
}

}