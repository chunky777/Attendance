package com.example.demo;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeRepository
                .findByEmployeeCode(username)
                .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));

        return User
                .withUsername(employee.getEmployeeCode())
                .password(employee.getPassword())
                .roles("USER")
                .build();
    }
}