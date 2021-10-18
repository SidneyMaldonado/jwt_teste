package jwt.exemplo.jwtsecurity.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jwt.exemplo.jwtsecurity.entities.User;
import jwt.exemplo.jwtsecurity.entities.UserAthentication;
import jwt.exemplo.jwtsecurity.repositories.UserRepository;
import jwt.exemplo.jwtsecurity.util.JwtUtil;

@RestController
@CrossOrigin
public class UserController {
	
	@Autowired
	public JwtUtil jwtutil;
	
	@Autowired
	private UserRepository repositorioUsuarios;
	
    @GetMapping("/")
    public String authorized() {
        return "U";
    }
	
	
	@PostMapping("/authenticate")
	public String gerarToken(@RequestBody UserAthentication usuario) throws Exception {
		
		String token = "fail";
		
		User u = repositorioUsuarios.findByNome(usuario.getUsuario());
		if (u!= null) {
			if (u.getSenha().equals(usuario.getSenha())) {
				// ta tudo certo - autenticado
			    token = gerarToken(u.getNome());
			}
		}
		return token;
	}
	
	private String gerarToken(String nome) {
		 Map<String, Object> claims = new HashMap<>();
		 return jwtutil.createToken(claims, nome);
	}

    	
}
