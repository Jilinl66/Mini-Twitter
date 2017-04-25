/**
 * HTTP Basic authentication
 * 
 * @author Jilin Liu
 */
package challenge.app;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import challenge.object.Person;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	UserRepository userRepository;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.authorizeRequests()
        .antMatchers("/h2-console/*").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .permitAll().successHandler(new CustomAuthenticationSuccessHandler())
        .and() 
        .logout().logoutUrl("/logout").logoutSuccessUrl("/")
        .deleteCookies("JSESSIONID")
        .invalidateHttpSession(true) ;
    	
        http.csrf().disable();
        http.headers().frameOptions().disable(); 
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       	for(Person person: userRepository.getAllPerson())
    		auth.inMemoryAuthentication().withUser(person.getName()).password("12345").roles("USER");
	}
    
    @Component
    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    	
	    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	    	    
		@Override
		public void onAuthenticationSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication) throws IOException,
				ServletException {
			
		    /*Set target URL to redirect*/
		    String redirectUrl = "http://localhost:8080/";
			getRedirectStrategy().sendRedirect(request, response, redirectUrl);
		}
		
		public RedirectStrategy getRedirectStrategy() {
			return redirectStrategy;
		}
	}
}