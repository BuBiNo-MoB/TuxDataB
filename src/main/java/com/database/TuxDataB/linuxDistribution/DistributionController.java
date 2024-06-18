package com.database.TuxDataB.linuxDistribution;

import com.database.TuxDataB.comment.CommentDTO;
import com.database.TuxDataB.comment.CommentService;
import com.database.TuxDataB.user.User;
import com.database.TuxDataB.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/distributions")
@RequiredArgsConstructor
public class DistributionController {

    private final LinuxDistributionService distributionService;
    private final CommentService commentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<LinuxDistributionDTO> createDistribution(@RequestBody LinuxDistributionDTO distributionDTO) {
        LinuxDistributionDTO createdDistribution = distributionService.create(distributionDTO);
        return new ResponseEntity<>(createdDistribution, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LinuxDistributionDTO> updateDistribution(@PathVariable Long id, @RequestBody LinuxDistributionDTO distributionDTO) {
        LinuxDistributionDTO updatedDistribution = distributionService.update(id, distributionDTO);
        return new ResponseEntity<>(updatedDistribution, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinuxDistributionDTO> getDistributionById(@PathVariable Long id) {
        LinuxDistributionDTO distributionDTO = distributionService.findById(id);
        return new ResponseEntity<>(distributionDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LinuxDistributionDTO>> getAllDistributions() {
        List<LinuxDistributionDTO> distributions = distributionService.findAll();
        return new ResponseEntity<>(distributions, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistribution(@PathVariable Long id) {
        distributionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        LinuxDistribution distribution = modelMapper.map(distributionService.findById(id), LinuxDistribution.class);
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userService.findOneByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        CommentDTO createdCommentDTO = commentService.create(commentDTO);
        return new ResponseEntity<>(createdCommentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByDistributionId(@PathVariable Long id) {
        List<CommentDTO> comments = commentService.findByDistributionId(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
