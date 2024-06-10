package SpringAuthApiBoilerplate.repository;

import java.util.Optional;
import SpringAuthApiBoilerplate.entity.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserRepository extends JpaRepository<CustomUser, String> {
    Optional<CustomUser> findByEmail(String email);

    CustomUser findByZname(String zname);
}
