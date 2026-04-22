package com.example.demo.service;

import com.example.demo.model.Doctor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    private static final String DATA_FILE = "doctors.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Doctor> loadDoctors() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try {
            return objectMapper.readValue(file, new TypeReference<List<Doctor>>() {});
        } catch (IOException e) {
            System.err.println("Error loading doctors data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void saveDoctors(List<Doctor> doctors) {
        try {
            objectMapper.writeValue(new File(DATA_FILE), doctors);
        } catch (IOException e) {
            System.err.println("Error saving doctors data: " + e.getMessage());
        }
    }
}
