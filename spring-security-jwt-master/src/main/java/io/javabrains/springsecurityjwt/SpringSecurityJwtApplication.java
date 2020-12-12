package io.javabrains.springsecurityjwt;

import io.javabrains.springsecurityjwt.filters.JwtRequestFilter;
import io.javabrains.springsecurityjwt.models.*;
import io.javabrains.springsecurityjwt.services.UserService;
import io.javabrains.springsecurityjwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class SpringSecurityJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityJwtApplication.class, args);
	}

}

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
class HelloWorldController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	private final UserService service;

	@RequestMapping({ "/hello" })
	public String firstPage() {
		return "Hello World";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		if(jwtTokenUtil.extractIssuer(authenticationRequest.getToken()).equals("https://accounts.google.com")) {
			System.out.println("aici ar trebui sa valideze dac tokenu ii de la google");
		}
		String token = authenticationRequest.getToken();

		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtTokenUtil.extractEmail(token), jwtTokenUtil.extractSubject(token))
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("User is not registered", e);
		}

		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(jwtTokenUtil.extractEmail(token));

		User user = service.getUser(userDetails.getUsername());

		final String jwt = jwtTokenUtil.generateToken(user);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));

	}

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public void registerUser(@RequestBody RegisterRequest registerRequest) throws Exception {
		String token = registerRequest.getToken();

		try {
			userDetailsService.loadUserByUsername(jwtTokenUtil.extractEmail(token));
		}catch (Exception e){
			UserDto userDto = new UserDto(jwtTokenUtil.extractEmail(token), jwtTokenUtil.extractSubject(token), registerRequest.isMonitor(), registerRequest.getLinkedEmail());
			service.signUpUser(userDto);
			System.out.println("User registered");
			return;
		}

		System.out.println("user allready exists");

	}

	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public void addUser(@RequestBody UserDto userDto) {
		service.signUpUser(userDto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/get")
	public ResponseEntity<?> getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return new ResponseEntity<>(service.getUser(auth.getName()), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/delete")
	public void deleteUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		service.deleteUser(auth.getName());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/cor")
	public void updateCor(@RequestBody Coordinates coordinates) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("dsd");
		service.updateCor(auth.getName(), coordinates.getCor());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/cor")
	public ResponseEntity<?> getCor() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = service.getCor(auth.getName());
		Float[] cor = new Float[]{user.getCorX(), user.getCorY()};
		return new ResponseEntity<>(new CorResponse(cor, user.getLSeen()), HttpStatus.OK);
	}


}

@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService myUserDetailsService;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
				.authorizeRequests().antMatchers("/user/register", "/user/login").permitAll().
						anyRequest().authenticated().and().
						exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}

}