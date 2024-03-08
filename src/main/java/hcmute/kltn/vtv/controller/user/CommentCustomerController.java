package hcmute.kltn.vtv.controller.user;

import hcmute.kltn.vtv.model.data.user.request.CommentRequest;
import hcmute.kltn.vtv.model.data.user.response.CommentResponse;
import hcmute.kltn.vtv.service.user.ICommentCustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customer/comment")
@RequiredArgsConstructor
public class CommentCustomerController {

    private final ICommentCustomerService commentCustomerService;

    @PostMapping("/add")
    public ResponseEntity<CommentResponse> addNewComment(@RequestBody CommentRequest request,
            HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        request.setUsername(username);
        request.setShop(false);
        request.validate();
        return ResponseEntity.ok(commentCustomerService.addNewComment(request));
    }

    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable UUID commentId,
            HttpServletRequest httpServletRequest) {
        String username = (String) httpServletRequest.getAttribute("username");
        return ResponseEntity.ok(commentCustomerService.deleteComment(commentId, username));
    }

}
