package com.database.TuxDataB.linuxDistribution;

import lombok.Data;

import java.util.Date;

@Data
public class LinuxDistributionDTO {
    private Long id;
    private String name;
    private String currentVersion;
    private Date releaseDate;
    private String description;
    private String officialWebsite;
    private String baseDistro;
    private String supportedArchitecture;
    private String packageType;
}
