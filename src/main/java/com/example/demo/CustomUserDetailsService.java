package com.example.demo;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String employeeCode) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new UsernameNotFoundException("社員コードが見つかりません: " + employeeCode));

        return new User(employee.getEmployeeCode(), employee.getPassword(), Collections.emptyList());
    }
    
}
