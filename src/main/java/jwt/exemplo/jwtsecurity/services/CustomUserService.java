package jwt.exemplo.jwtsecurity.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jwt.exemplo.jwtsecurity.entities.User;
import jwt.exemplo.jwtsecurity.repositories.UserRepository;

@Service
public class CustomUserService implements UserDetailsService {
	
	@Autowired
	public UserRepository repositorio;
	
	public User findByNome( String nome ) {
		
		return repositorio.findByNome(nome);
	}
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repositorio.findByNome(username);
        return new org.springframework.security.core.userdetails.User(user.getNome(), user.getSenha(), new ArrayList<>());
    }
    

}
