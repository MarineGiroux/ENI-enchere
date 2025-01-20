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
				"SELECT u.email, r.role FROM USERS u JOIN ROLES r ON u.idRole = r.idRole WHERE u.email = ?"
		);
		return jdbcUserDetailsManager;
	}
	


	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.GET, "/").permitAll()
				.requestMatchers(HttpMethod.POST, "/").permitAll()
				.requestMatchers(HttpMethod.GET, "/user/inscription").permitAll()
				.requestMatchers(HttpMethod.POST, "/user/inscription").permitAll()
				.requestMatchers(HttpMethod.GET, "/login").permitAll()
				.requestMatchers(HttpMethod.POST, "/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/logout").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/logout").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.GET, "/profil").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.POST, "/profil").hasAnyRole("ADMIN", "MEMBRE")
				.requestMatchers(HttpMethod.GET, "/css/*").permitAll()
				.requestMatchers(HttpMethod.GET, "/images/*").permitAll()
				.anyRequest().permitAll()
				
		);
		
		// utilisation du formulaire de connexion de Spring security
		//http.formLogin(Customizer.withDefaults());
		
		//utilisation du formulaire de connexion personnalisé
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
				.deleteCookies("JSESSIONID") // suppression du cookie de session
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // definit l'url permettant la deconnection
				.logoutSuccessUrl("/") // url appelée suite à la déconnexion
				.permitAll()
				);
		
		
		return http.build();
		
		
	}
	
	
}
