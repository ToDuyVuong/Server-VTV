package hcmute.kltn.vtv.model.data.guest;

import hcmute.kltn.vtv.model.dto.user.CommentDTO;
import hcmute.kltn.vtv.model.extra.ResponseAbstract;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ListCommentResponse extends ResponseAbstract {

    private int count;
    private UUID reviewId;
    private List<CommentDTO> commentDTOs;
}
