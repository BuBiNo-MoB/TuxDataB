package com.database.TuxDataB.comment;

import com.database.TuxDataB.linuxDistribution.LinuxDistribution;
import com.database.TuxDataB.linuxDistribution.LinuxDistributionRepository;
import com.database.TuxDataB.user.User;
import com.database.TuxDataB.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final LinuxDistributionRepository distributionRepository;
    private final UserRepository userRepository;

    public CommentDTO create(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());

        // Associa la distribuzione al commento
        LinuxDistribution distribution = distributionRepository.findById(commentDTO.getDistributionId())
                .orElseThrow(() -> new RuntimeException("Distribution not found"));
        comment.setDistribution(distribution);

        // Associa l'utente al commento
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        comment.setUser(user);

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    public List<CommentDTO> findAll() {
        return commentRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CommentDTO> findByDistributionId(Long distributionId) {
        return commentRepository.findByDistributionId(distributionId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDTO convertToDto(Comment comment) {
        CommentDTO commentDTO = modelMapper.map(comment, CommentDTO.class);
        // Verifica che l'utente non sia null prima di accedere al suo username
        if (comment.getUser() != null) {
            commentDTO.setUsername(comment.getUser().getUsername());
        }
        return commentDTO;
    }
}
