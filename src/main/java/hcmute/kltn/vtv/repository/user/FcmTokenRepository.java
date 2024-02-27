package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, UUID> {
    Optional<FcmToken> findByFcmToken(String fcmToken);

    boolean existsByFcmToken(String fcmToken);

    Optional<List<FcmToken>> findAllByUsername(String username);

    boolean existsByUsername(String username);

    void deleteByFcmToken(String fcmToken);


}
