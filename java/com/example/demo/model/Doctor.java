package com.example.demo.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    private String id;
    private String name;
    private String specialty;
    private int experienceYears;
    private String phone;
    private String address;
    private String joinDate;
    private String avatar;
}