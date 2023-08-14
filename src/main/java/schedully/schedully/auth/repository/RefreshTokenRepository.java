package schedully.schedully.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import schedully.schedully.auth.domain.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    public RefreshToken findByMemberId(Long memberId);
}
