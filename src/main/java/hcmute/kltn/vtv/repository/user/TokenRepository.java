package hcmute.kltn.vtv.repository.user;

import hcmute.kltn.vtv.model.entity.user.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

  @Query(value = """
      select t from Token t inner join Customer c\s
      on t.customer.customerId = c.customerId\s
      where c.customerId = :customerId and (t.expired = false or t.revoked = false)\s
      """)
  List<Token> findAllValidTokenByCustomer(Long customerId);

  List<Token> findAllByExpired(boolean expired);

  List<Token> findAllByExpiredAndRevoked(boolean expired, boolean revoked);


  Optional<Token> findByCustomerCustomerIdAndToken(Long customerId, String token);




  // @Query("select t from Token t inner join Customer c " +
  // "on t.customer.customerId = c.customerId " +
  // "where c.customerId = :customerId and (t.expired = false or t.revoked =
  // false)")
  // List<Token> findAllValidTokenByCustomer(@Param("customerId") Long
  // customerId);

  Optional<Token> findByToken(String token);

}