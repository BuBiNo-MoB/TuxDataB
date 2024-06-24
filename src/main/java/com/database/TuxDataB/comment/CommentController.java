package com.database.TuxDataB.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDTO> create(@RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentService.create(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CommentDTO>> findAll() {
        List<CommentDTO> comments = commentService.findAll();
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/distribution/{distributionId}")
    public ResponseEntity<List<CommentDTO>> findByDistributionId(@PathVariable Long distributionId) {
        List<CommentDTO> comments = commentService.findByDistributionId(distributionId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}