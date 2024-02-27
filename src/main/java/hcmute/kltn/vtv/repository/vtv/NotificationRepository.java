package hcmute.kltn.vtv.repository.vtv;

import hcmute.kltn.vtv.model.entity.vtv.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
