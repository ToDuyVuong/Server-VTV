package hcmute.kltn.vtv.model.data.chat.request;

import hcmute.kltn.vtv.util.exception.BadRequestException;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {
    private String content;
    private Date date;
    private String senderUsername;
    private String receiverUsername;
    private UUID romChatId;


    public void validate(ChatMessageRequest request) {
        if (request == null) {
            throw new BadRequestException("Yêu cầu không hợp lệ");
        }
        if (request.getContent() == null) {
            throw new BadRequestException("Nội dung tin nhắn không được để trống");
        }
        if (request.getSenderUsername() == null) {
            throw new BadRequestException("Tên người gửi không được để trống");
        }
        if (request.getReceiverUsername() == null) {
            throw new BadRequestException("Tên người nhận không được để trống");
        }
        if (request.getRomChatId() == null) {
            throw new BadRequestException("Mã phòng chat không được để trống");
        }
        trim();
    }

    public void trim() {
        this.content = this.content.trim();
        this.senderUsername = this.senderUsername.trim();
        this.receiverUsername = this.receiverUsername.trim();
    }

}
