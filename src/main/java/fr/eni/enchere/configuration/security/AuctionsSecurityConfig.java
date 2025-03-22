package fr.eni.enchere.configuration.security;

import fr.eni.enchere.bll.AuctionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class AuctionsSecurityConfig {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuctionsSecurityConfig.class);

	@Autowired
	AuctionsService auctionsService;

	@Bean
	UserDetailsManager userDetailsManager(DataSource dataSource) {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.setUsersByUsernameQuery(
				"SELECT email, password, 1 FROM USERS WHERE email = ?"
		);
		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
				"SELECT u.email, CONCAT('ROLE_', r.role) FROM USERS u JOIN ROLES r ON u.idRole = r.idRole WHERE u.email = ?"
		);
		return jdbcUserDetailsManager;
	}
	


	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, UserDetailsManager userDetailsManager) throws Exception {
		http.userDetailsService(userDetailsManager);
		
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.GET, "/").permitAll()
				.requestMatchers(HttpMethod.GET, "/css/*").permitAll()
				.requestMatchers(HttpMethod.GET, "/images/*").permitAll()
				.requestMatchers(HttpMethod.GET, "/user/inscription").permitAll()
				.requestMatchers(HttpMethod.POST, "/user/inscription").permitAll()
				.requestMatchers(HttpMethod.GET, "/login").permitAll()
				.requestMatchers(HttpMethod.POST, "/login").permitAll()
				.requestMatchers(HttpMethod.POST, "/logout").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.GET, "/user/profile").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/user/profile").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/user/update").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/user/delete").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.GET, "/sales").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/sales").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.GET, "/sales/detail/{id}").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/sales/edit/{id}").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/sales/deleteArticle/{id}").hasAnyRole("ADMIN", "MEMBRE")
				.anyRequest().permitAll()
		);
		http.formLogin(form ->
				form.loginPage("/login")
						.permitAll()
						.defaultSuccessUrl("/")
						.successHandler((request, response, authentication) -> {
							auctionsService.closeOutdatedAuctions(authentication.getName());
							response.sendRedirect(request.getContextPath());
						})
		);
		http.logout(logout->
				logout.invalidateHttpSession(true)
				.clearAuthentication(true)
				.deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.permitAll()
				);
		
		
		return http.build();
		
		
	}
	
	
}
