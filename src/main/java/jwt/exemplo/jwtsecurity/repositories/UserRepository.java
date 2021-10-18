package jwt.exemplo.jwtsecurity.repositories;

import org.springframework.stereotype.Repository;
import jwt.exemplo.jwtsecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByNome( String nome );

}
