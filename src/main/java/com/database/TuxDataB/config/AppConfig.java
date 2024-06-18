package com.database.TuxDataB.config;

import com.database.TuxDataB.comment.Comment;
import com.database.TuxDataB.comment.CommentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(CommentDTO.class, Comment.class).addMappings(mapper -> {
            mapper.skip(Comment::setId);
            mapper.skip(Comment::setDistribution);
            mapper.skip(Comment::setUser);
        });

        return modelMapper;
    }
}
