package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void 社員登録ができること() {
        // 準備
        Employee emp = new Employee();
        emp.setEmployeeCode("T001");
        emp.setName("テスト太郎");
        emp.setEmail("test@example.com");
        emp.setPassword("password");

        // 実行
        Employee saved = employeeService.register(emp);

        // 検証
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmployeeCode()).isEqualTo("T001");
        assertThat(saved.getPassword()).isNotEqualTo("password"); // ハッシュ化されている
    }
}
