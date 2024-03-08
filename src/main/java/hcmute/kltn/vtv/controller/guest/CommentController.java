package hcmute.kltn.vtv.controller.guest;

import hcmute.kltn.vtv.model.data.guest.ListCommentResponse;
import hcmute.kltn.vtv.service.guest.ICommentService;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @GetMapping("/get/{reviewId}")
    public ResponseEntity<ListCommentResponse> getComments(@PathVariable UUID reviewId) {
        if (reviewId == null) {
            throw new NotFoundException("Mã đánh giá không được để trống!");
        }
        return ResponseEntity.ok(commentService.getComments(reviewId));
    }

}
