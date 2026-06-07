package filmly.repository;

import filmly.model.User;
import filmly.model.UserVerificationToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<UserVerificationToken,Long> {
    Optional<UserVerificationToken> findByUserAndType(User user,
                                                      UserVerificationToken.TokenType type);

    Optional<UserVerificationToken> findByUser(User user);
}

