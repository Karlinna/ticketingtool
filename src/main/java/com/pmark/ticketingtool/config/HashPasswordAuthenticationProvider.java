package com.pmark.ticketingtool.config;


import com.pmark.ticketingtool.model.entity.User;
import com.pmark.ticketingtool.model.repositories.UsersRepository;
import com.pmark.ticketingtool.utility.JsonFactory;
import com.pmark.ticketingtool.utility.Tools;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 *
 * This class is responsible for checking validity of username + password pairs as they have sent via basic authentication<br>
 * This can be done here via DB or whatever
 */
@Component
public class HashPasswordAuthenticationProvider implements AuthenticationProvider {

	@Inject
	private UsersRepository usersRepository;

	/*http.cors().and().csrf().disable()
            .authorizeRequests().antMatchers("/authorize").permitAll()
            .and()
            .authorizeRequests().antMatchers("/private").authenticated()
            .and()
            .authorizeRequests().antMatchers("/public").permitAll()
            .and()
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            .addFilter(new JWTAuthenticationFilter(authenticationManager()))
            .sessionManagement().sessionCreationPolicy(STATELESS);*/
	//http.httpBasic().and().authorizeRequests().anyRequest().permitAll().and().csrf().disable();
	private final static Logger log = LoggerFactory.getLogger(HashPasswordAuthenticationProvider.class);
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = null;
		String password = null;
		try{
		 username = authentication.getName();
         password = Tools.generateHash(authentication.getCredentials().toString(), 0);

		}
		catch (Exception ex) {
			log.error("Error during auth: " ,ex);
		}

        if(username==null || username.isEmpty()){
        	throw new BadCredentialsException("User name can not be empty");
        }
        if(password==null || password.isEmpty()){
        	throw new BadCredentialsException("Password can not be empty");
        }
        User user = usersRepository.findFirstByUserAndPass(username, password);
        if(user==null){
        	throw new BadCredentialsException("Username or password does not match!");
        }

        return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
	}
		
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@ExceptionHandler(BadCredentialsException.class)
	private String badCredentialException(BadCredentialsException bex){

		log.error("Error during authentication : ", bex);

		return JsonFactory.error(bex.getMessage());
	}

}
