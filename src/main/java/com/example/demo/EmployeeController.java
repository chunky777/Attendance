package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Controller
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;

    //登録画面
    @GetMapping("/employee/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "employee_register";
    }

    //登録処理
    @PostMapping("/employee/register")
    public String registerEmployee(@ModelAttribute Employee employee) {
        employeeService.register(employee);
        return "redirect:/employee/register?success";
    }
}
