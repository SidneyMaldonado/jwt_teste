package jwt.exemplo.jwtsecurity.filter;

import java.io.Console;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jwt.exemplo.jwtsecurity.entities.User;
import jwt.exemplo.jwtsecurity.repositories.UserRepository;
import jwt.exemplo.jwtsecurity.services.CustomUserService;
import jwt.exemplo.jwtsecurity.util.JwtUtil;

@Component
public class JwFilter extends OncePerRequestFilter {

	@Autowired
	public JwtUtil jwtutil;
	
	public String nomeUsuario;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(nomeUsuario).parseClaimsJws(token).getBody();
    }
    
    public Boolean validateToken(String token, User userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getNome()));
    }
    
    @Autowired
    public CustomUserService servico;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)  {
    	
    	try {
    	System.out.println("Teste do filtro");
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        String token = null;
        String userName = null;
        System.out.println(authorizationHeader);
        System.out.println(httpServletRequest);
        
        

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            System.out.println(token);
            nomeUsuario = jwtutil.extractUsername(token);
        }
        
        if (nomeUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = servico.loadUserByUsername(userName);

            if (jwtutil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    	} catch (Exception e) {
    		System.out.println(e.getMessage());
    	}
    }	
	
}
