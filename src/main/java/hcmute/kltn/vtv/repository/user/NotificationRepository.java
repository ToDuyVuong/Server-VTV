package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Optional<Page<Notification>> findByRecipientOrderByCreateAtDesc(String recipient, Pageable pageable);

    boolean existsByNotificationId(UUID notificationId);

    Optional<Notification> findByNotificationIdAndRecipient(UUID notificationId, String recipient);

}
