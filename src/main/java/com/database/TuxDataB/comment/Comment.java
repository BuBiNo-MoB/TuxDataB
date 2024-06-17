package com.database.TuxDataB.comment;

import com.database.TuxDataB.linuxDistribution.LinuxDistribution;
import com.database.TuxDataB.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "distribution_id")
    private LinuxDistribution distribution;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String text;
}