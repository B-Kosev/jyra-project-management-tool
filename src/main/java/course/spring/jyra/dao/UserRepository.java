package course.spring.jyra.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import course.spring.jyra.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByUsername(String username);
}
