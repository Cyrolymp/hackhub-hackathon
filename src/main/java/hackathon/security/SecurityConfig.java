package hackathon.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity( jsr250Enabled = true, securedEnabled = true )
public class SecurityConfig {

	// -------
	// SecurityFilterChain

	@Bean
	SecurityFilterChain securityFilterChain(
			HttpSecurity httpSecurity,
			UserDetailsService userDetailsService,
			RememberMeServices rememberMeServices )
			throws Exception {

		httpSecurity
				.csrf( csrf -> csrf.disable() ).formLogin( formLogin -> formLogin.loginPage( "/login" ) )
				.rememberMe( remember -> remember.rememberMeServices( rememberMeServices ) )
				.logout( logout -> logout.logoutUrl( "/logout" ).logoutSuccessUrl( "/disconnected" ) )
				.exceptionHandling( req -> req.accessDeniedPage( "/accessDenied" ) )
				.authorizeHttpRequests(
						auth -> auth.requestMatchers( "/*", "/hackathons/**", "/equipes/**", "/css/**", "/img/**", "/js/**", "/lib/**" ).permitAll() )
				.authorizeHttpRequests( auth -> auth.anyRequest().authenticated() );

		return httpSecurity.build();

	}

	// -------
	// RememberMeServices

	@Bean
	RememberMeServices rememberMeServices(
			UserDetailsService userDetailsService ) {

		var services = new TokenBasedRememberMeServices(
				"3c707a4c-34c9-4421-a3d4-85f20db0359e",
				userDetailsService );
		services.setAlwaysRemember( false );
		services.setTokenValiditySeconds( 7 * 24 * 60 * 60 );
		return services;
	}

	// -------
	// PasswordEncoder

	@Bean
	PasswordEncoder passwordEncoder() {
		var dpe = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
		dpe.setDefaultPasswordEncoderForMatches( new BCryptPasswordEncoder() );
		return dpe;
	}
}
