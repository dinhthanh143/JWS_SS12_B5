package com.example.demo.controller;

import com.example.demo.model.Doctor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Value("${app.hospital.name}")
    private String hospitalName;

    private List<Doctor> doctorList = new ArrayList<>();

    public DoctorController() {
        doctorList.add(new Doctor("DOC01", "Dr. Nguyen Van A", "Noi khoa", 10, "0912345678", "Ha Noi", "2020-01-01", "avatar1.png"));
    }

    @GetMapping("/list")
    public String list(Model model, @RequestParam(required = false) String phone) {
        model.addAttribute("hName", hospitalName);
        List<Doctor> results = (phone == null || phone.isEmpty()) ? doctorList :
                doctorList.stream().filter(d -> d.getPhone().contains(phone)).collect(Collectors.toList());
        model.addAttribute("doctors", results);
        return "doctor-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "doctor-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Doctor doctor) {
        // Logic de don gian: Neu ton tai thi xoa di add lai (Update)
        doctorList.removeIf(d -> d.getId().equals(doctor.getId()));
        doctorList.add(doctor);
        return "redirect:/doctors/list";
    }
}