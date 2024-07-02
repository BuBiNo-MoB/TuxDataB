package com.database.TuxDataB.linuxDistribution;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinuxDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Aggiungi questo campo
    private String description;
    private String officialWebsite;
    private String baseDistro;
    private String supportedArchitecture;
    private String packageType;
    private String desktopEnvironment;
    private String logoUrl;
    private String desktopImageUrl;

    public LinuxDistribution(String name, String description, String officialWebsite, String baseDistro, String supportedArchitecture, String packageType, String desktopEnvironment, String logoUrl, String desktopImageUrl) {
        this.name = name;
        this.description = description;
        this.officialWebsite = officialWebsite;
        this.baseDistro = baseDistro;
        this.supportedArchitecture = supportedArchitecture;
        this.packageType = packageType;
        this.desktopEnvironment = desktopEnvironment;
        this.logoUrl = logoUrl;
        this.desktopImageUrl = desktopImageUrl;
    }
}
