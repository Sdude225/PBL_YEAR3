package io.javabrains.springsecurityjwt;

import io.javabrains.springsecurityjwt.filters.JwtRequestFilter;
import io.javabrains.springsecurityjwt.models.AuthenticationRequest;
import io.javabrains.springsecurityjwt.models.AuthenticationResponse;
import io.javabrains.springsecurityjwt.models.UserDto;
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
import org.springframework.security.core.authority.AuthorityUtils;
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

	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public ResponseEntity<?> test(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
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

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));

	}

	@RequestMapping(method = RequestMethod.POST, value = "/register")
	public void registerUser(@RequestBody AuthenticationRequest authenticationRequest) {
		String token = authenticationRequest.getToken();
		UserDto userDto = new UserDto(jwtTokenUtil.extractEmail(token), jwtTokenUtil.extractSubject(token));
		service.signUpUser(userDto);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/add")
	public void addUser(@RequestBody UserDto userDto) {
		service.signUpUser(userDto);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{email}")
	public ResponseEntity<?> getUser(@PathVariable String email) {
		return new ResponseEntity<>(service.getUser(email), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/id{email}")
	public void deleteUser(@PathVariable String email) {
		service.deleteUser(email);
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
				.authorizeRequests().antMatchers("/user/register", "/user/test").permitAll().
						anyRequest().authenticated().and().
						exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

	}

}