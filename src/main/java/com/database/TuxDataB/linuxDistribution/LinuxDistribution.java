package com.database.TuxDataB.linuxDistribution;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "linux_distribution")
public class LinuxDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Date releaseDate;
    private String description;
    private String officialWebsite;
    private String baseDistro;
    private String supportedArchitecture;
    private String packageType;
    private String desktopEnvironment;
    private String logoUrl;
    private String desktopImageUrl;
    private String currentVersion;

    public LinuxDistribution(String name, String description, Date releaseDate, String officialWebsite, String baseDistro, String supportedArchitecture, String packageType, String desktopEnvironment, String logoUrl, String desktopImageUrl, String currentVersion) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.officialWebsite = officialWebsite;
        this.baseDistro = baseDistro;
        this.supportedArchitecture = supportedArchitecture;
        this.packageType = packageType;
        this.desktopEnvironment = desktopEnvironment;
        this.logoUrl = logoUrl;
        this.desktopImageUrl = desktopImageUrl;
        this.currentVersion = currentVersion;
    }
}
