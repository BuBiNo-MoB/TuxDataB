package com.database.TuxDataB.linuxDistribution;

import com.database.TuxDataB.comment.Comment;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "linux_distributions")
public class LinuxDistribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String currentVersion;
    private Date releaseDate;

    @Lob
    private String description;
    private String officialWebsite;
    private String baseDistro;
    private String supportedArchitecture;
    private String packageType;

    @OneToMany(mappedBy = "distribution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    private boolean isAvailable = true;
}