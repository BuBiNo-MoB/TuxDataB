package com.database.TuxDataB.comment;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String text;
    private String username;
    private Long distributionId;
    private Long userId;
}

