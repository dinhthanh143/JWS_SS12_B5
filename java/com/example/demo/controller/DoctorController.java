package com.example.demo.controller;

import com.example.demo.model.Doctor;
import com.example.demo.service.DataService;
import com.example.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/doctors")
public class DoctorController {

    @Value("${app.hospital.name}")
    private String hospitalName;

    @Autowired
    private ImageService imageService;

    @Autowired
    private DataService dataService;

    private List<Doctor> doctorList;

    @PostConstruct
    public void init() {
        doctorList = dataService.loadDoctors();
        if (doctorList.isEmpty()) {
            doctorList.add(new Doctor("DOC01", "Dr. Nguyen Van A", "Noi khoa", 10, "0912345678", "Ha Noi", "2020-01-01", "avatar1.png"));
            dataService.saveDoctors(doctorList);
        }
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
    public String addForm(Model model, @RequestParam(required = false) String id) {
        Doctor doctor;
        if (id != null && !id.isEmpty()) {
            doctor = doctorList.stream()
                    .filter(d -> d.getId().equals(id))
                    .findFirst()
                    .orElse(new Doctor());
        } else {
            doctor = new Doctor();
        }
        model.addAttribute("doctor", doctor);
        return "doctor-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Doctor doctor, 
                      @RequestParam(required = false) MultipartFile avatarFile,
                      Model model) {
        try {
            if (avatarFile != null && !avatarFile.isEmpty()) {
                if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                    imageService.deleteImage(doctor.getAvatar());
                }
                String fileName = imageService.saveImage(avatarFile);
                doctor.setAvatar(fileName);
            }

            doctorList.removeIf(d -> d.getId().equals(doctor.getId()));
            
            doctorList.add(doctor);
            
            dataService.saveDoctors(doctorList);
            
        } catch (Exception e) {
            model.addAttribute("error", "Lá»i khi lÆ°u thÃ´ng tin: " + e.getMessage());
            model.addAttribute("doctor", doctor);
            return "doctor-form";
        }
        
        return "redirect:/doctors/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        doctorList.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .ifPresent(doctor -> {
                    if (doctor.getAvatar() != null && !doctor.getAvatar().isEmpty()) {
                        imageService.deleteImage(doctor.getAvatar());
                    }
                    doctorList.removeIf(d -> d.getId().equals(id));
                    
                    dataService.saveDoctors(doctorList);
                });
        return "redirect:/doctors/list";
    }
}
