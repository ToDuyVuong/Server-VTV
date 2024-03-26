package hcmute.kltn.vtv.service.chat.impl;

import hcmute.kltn.vtv.model.data.chat.response.ListRoomChatsPageResponse;
import hcmute.kltn.vtv.model.data.chat.response.RoomChatResponse;
import hcmute.kltn.vtv.model.dto.chat.RoomChatDTO;
import hcmute.kltn.vtv.model.entity.chat.RoomChat;
import hcmute.kltn.vtv.model.extra.Status;
import hcmute.kltn.vtv.repository.chat.RoomChatRepository;
import hcmute.kltn.vtv.service.chat.IRoomChatService;
import hcmute.kltn.vtv.service.user.ICustomerService;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomChatServiceImpl implements IRoomChatService {

    private final RoomChatRepository roomChatRepository;
    private final ICustomerService customerService;

    @Override
    @Transactional
    public RoomChatResponse addNewRoomChat(String senderUsername, String receiverUsername) {

        String message = "Tạo phòng chat thành công!";

        checkUsernameReceiver(receiverUsername);
        int roomChatExist = checkRoomChatExist(senderUsername, receiverUsername);

        if (roomChatExist != 0) {
            message = "Lấy thông tin phòng chat thành công!";
        }

        RoomChat roomChat = createOrUpdateRoomChat(senderUsername, receiverUsername, roomChatExist);

        try {
            roomChatRepository.save(roomChat);

            return roomChatResponse(roomChat, message, roomChatExist == 0 ? "Success" : "OK");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi hệ thống phòng chat! Vui lòng thử lại sau.");
        }
    }


    @Override
    public RoomChatResponse deleteRoomChatByUsername(UUID roomChatId, String username) {

        RoomChat roomChat = getRoomChatById(roomChatId);

        checkDeleteByUsername(username, roomChat);

        roomChat.setSenderDelete(username.equals(roomChat.getSenderUsername()) || roomChat.isSenderDelete());
        roomChat.setReceiverDelete(username.equals(roomChat.getReceiverUsername()) || roomChat.isReceiverDelete());
        roomChat.setUpdatedAt(LocalDateTime.now());
        try {
            roomChatRepository.save(roomChat);

            return roomChatResponse(roomChat, "Xóa phòng chat thành công!", "Success");
        } catch (Exception e) {
            throw new InternalServerErrorException("Lỗi hệ thống khi xóa phòng chat! Vui lòng thử lại sau.");
        }
    }


    @Override
    public RoomChat getRoomChatBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername) {

        int roomChatExist = checkRoomChatExist(senderUsername, receiverUsername);
        if (roomChatExist == 0) {
            throw new NotFoundException("Không tìm thấy phòng chat!");
        } else {
            return getRoomChat(senderUsername, receiverUsername, roomChatExist);
        }
    }

    @Override
    public ListRoomChatsPageResponse getListRoomChatsPageByUsername(String username, int page, int size) {

        try {
            Page<RoomChat> roomChatPage = roomChatRepository
                    .findAllBySenderUsernameOrReceiverUsername(username, username,
                            PageRequest.of(page - 1, size))
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy phòng chat!"));

            return listRoomChatsPageResponse(roomChatPage, page, size);
        } catch (Exception e) {
            throw new InternalServerErrorException(
                    "Lỗi hệ thống! Lấy danh sách phòng chat thất bại! Vui lòng thử lại sau." + e.getMessage());
        }

    }

    @Override
    public RoomChat getRoomChatById(UUID roomChatId) {
        return roomChatRepository.findById(roomChatId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phòng chat!"));
    }

    @Override
    @Transactional
    public void updateDateRoomChatById(UUID roomChatId, Date date, String lastMessage, String senderUsername) {
        RoomChat roomChat = getRoomChatById(roomChatId);
        roomChat.setLastDate(date);
        roomChat.setLastMessage(lastMessage);
        roomChat.setSenderSeen(checkSender(senderUsername, roomChat));
        roomChat.setReceiverSeen(checkReceiver(senderUsername, roomChat));
        roomChat.setReceiverDelete(false);
        roomChat.setSenderDelete(false);

        try {
            roomChatRepository.save(roomChat);
        } catch (Exception e) {
            throw new InternalServerErrorException(
                    "Lỗi hệ thống! Cập nhật thời gian phòng chat thất bại! Vui lòng thử lại sau.");
        }
    }




    private void checkDeleteByUsername(String username, RoomChat roomChat) {
        if ((username.equals(roomChat.getSenderUsername()) && roomChat.isSenderDelete()) ||
                (username.equals(roomChat.getReceiverUsername()) && roomChat.isReceiverDelete())) {
            throw new NotFoundException("Bạn đã xóa phòng chat này!");
        }
    }

    private void checkUsernameReceiver(String receiverUsername) {
        if (!customerService.checkUsernameExist(receiverUsername)) {
            throw new NotFoundException("Tài khoản không tồn tại!");
        }
    }

    private RoomChat createOrUpdateRoomChat(String senderUsername, String receiverUsername, int roomChatExist) {
        RoomChat roomChat = new RoomChat();

        if (roomChatExist == 1 || roomChatExist == 2) {
            roomChat = getRoomChat(senderUsername, receiverUsername, roomChatExist);

        } else {
            roomChat.setSenderUsername(senderUsername);
            roomChat.setReceiverUsername(receiverUsername);
            roomChat.setCreatedAt(LocalDateTime.now());
            roomChat.setLastDate(new Date());
            roomChat.setSenderDelete(true);
            roomChat.setReceiverDelete(true);
            roomChat.setSenderSeen(true);
            roomChat.setReceiverSeen(true);
        }

        roomChat.setStatus(Status.ACTIVE);
        roomChat.setUpdatedAt(LocalDateTime.now());

        return roomChat;
    }

    private boolean checkSender(String senderUsername, RoomChat roomChat) {
        return senderUsername.equals(roomChat.getSenderUsername()) || roomChat.isSenderSeen();
    }

    private boolean checkReceiver(String receiverUsername, RoomChat roomChat) {

        return receiverUsername.equals(roomChat.getReceiverUsername()) || roomChat.isReceiverSeen();
    }





    @Override
    public void checkRequestPageParams(int page, int size) {
        if (page <= 0) {
            throw new NotFoundException("Trang không được nhỏ hơn 0!");
        }
        if (size <= 0) {
            throw new NotFoundException("Kích thước trang không được nhỏ hơn 0!");
        }
        if (size > 50) {
            throw new NotFoundException("Kích thước trang không được lớn hơn 50!");
        }
    }

    private ListRoomChatsPageResponse listRoomChatsPageResponse(Page<RoomChat> roomChatPage, int page, int size) {
        ListRoomChatsPageResponse response = new ListRoomChatsPageResponse();
        response.setRoomChatDTOs(RoomChatDTO.convertEntitiesToDTOs(roomChatPage.getContent()));
        response.setTotalPage(roomChatPage.getTotalPages());
        response.setCount(roomChatPage.getContent().size());
        response.setPage(page);
        response.setSize(size);
        response.setCode(200);
        response.setStatus("OK");
        response.setMessage("Lấy danh sách phòng chat thành công!");
        return response;
    }

    private RoomChatResponse roomChatResponse(RoomChat roomChat, String message, String status) {
        RoomChatResponse response = new RoomChatResponse();
        response.setRoomChatDTO(RoomChatDTO.convertEntitiesToDTOs(roomChat));
        response.setMessage(message);
        response.setStatus(status);
        response.setCode(200);
        return response;
    }

    private int checkRoomChatExist(String senderUsername, String receiverUsername) {
        if (roomChatRepository.existsBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername)) {
            return 1;
        }
        if (roomChatRepository.existsBySenderUsernameAndReceiverUsername(receiverUsername, senderUsername)) {
            return 2;
        }
        return 0;
    }

    public RoomChat getRoomChat(String senderUsername, String receiverUsername, int roomChatExist) {
        if (roomChatExist == 1) {
            return roomChatRepository.findBySenderUsernameAndReceiverUsername(senderUsername, receiverUsername)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy phòng chat!"));
        }
        return roomChatRepository.findBySenderUsernameAndReceiverUsername(receiverUsername, senderUsername)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phòng chat!"));
    }

}
