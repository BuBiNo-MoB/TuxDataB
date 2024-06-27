package com.database.TuxDataB.linuxDistribution;

import com.database.TuxDataB.comment.Comment;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/distributions")
@RequiredArgsConstructor
public class DistributionController {

    private final LinuxDistributionService distributionService;
    private final CommentService commentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LinuxDistributionDTO> createDistribution(
            @RequestPart("distribution") LinuxDistributionDTO distributionDTO,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "desktopImage", required = false) MultipartFile desktopImage) throws IOException {

        LinuxDistributionDTO createdDistribution = distributionService.create(distributionDTO, logo, desktopImage);
        return new ResponseEntity<>(createdDistribution, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LinuxDistributionDTO> updateDistribution(
            @PathVariable Long id,
            @RequestPart("distribution") LinuxDistributionDTO distributionDTO,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "desktopImage", required = false) MultipartFile desktopImage) throws IOException {

        LinuxDistributionDTO updatedDistribution = distributionService.update(id, distributionDTO, logo, desktopImage);
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

        Comment comment = new Comment();
        comment.setDistribution(distribution);
        comment.setUser(user);
        comment.setText(commentDTO.getText());

        CommentDTO createdCommentDTO = modelMapper.map(commentService.create(modelMapper.map(comment, CommentDTO.class)), CommentDTO.class);
        return new ResponseEntity<>(createdCommentDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByDistributionId(@PathVariable Long id) {
        List<CommentDTO> comments = commentService.findByDistributionId(id);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<LinuxDistributionDTO>> searchByName(@RequestParam String keyword) {
        List<LinuxDistributionDTO> distributions = distributionService.searchByName(keyword);
        return new ResponseEntity<>(distributions, HttpStatus.OK);
    }

    @GetMapping("/searchByDesktopEnvironment")
    public ResponseEntity<List<LinuxDistributionDTO>> searchByDesktopEnvironment(@RequestParam String desktopEnvironment) {
        List<LinuxDistributionDTO> distributions = distributionService.findByDesktopEnvironment(desktopEnvironment);
        return new ResponseEntity<>(distributions, HttpStatus.OK);
    }

    @PostMapping("/{id}/logo")
    public ResponseEntity<String> uploadLogo(@PathVariable Long id, @RequestParam("logo") MultipartFile logo) throws IOException {
        String logoUrl = distributionService.uploadLogo(id, logo);
        return new ResponseEntity<>(logoUrl, HttpStatus.OK);
    }

    @PostMapping("/{id}/desktopImage")
    public ResponseEntity<String> uploadDesktopImage(@PathVariable Long id, @RequestParam("desktopImage") MultipartFile desktopImage) throws IOException {
        String desktopImageUrl = distributionService.uploadDesktopImage(id, desktopImage);
        return new ResponseEntity<>(desktopImageUrl, HttpStatus.OK);
    }

    @PutMapping("/{id}/logo")
    public ResponseEntity<String> updateLogo(@PathVariable Long id, @RequestParam("logo") MultipartFile logo) throws IOException {
        String logoUrl = distributionService.updateLogo(id, logo);
        return new ResponseEntity<>(logoUrl, HttpStatus.OK);
    }

    @PutMapping("/{id}/desktopImage")
    public ResponseEntity<String> updateDesktopImage(@PathVariable Long id, @RequestParam("desktopImage") MultipartFile desktopImage) throws IOException {
        String desktopImageUrl = distributionService.updateDesktopImage(id, desktopImage);
        return new ResponseEntity<>(desktopImageUrl, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/logo")
    public ResponseEntity<Void> deleteLogo(@PathVariable Long id) throws IOException {
        distributionService.deleteLogo(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/desktopImage")
    public ResponseEntity<Void> deleteDesktopImage(@PathVariable Long id) throws IOException {
        distributionService.deleteDesktopImage(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
