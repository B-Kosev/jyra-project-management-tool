package course.spring.jyra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import course.spring.jyra.service.UserService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
//        http.csrf().disable()
//                .authorizeRequests()
//                    .antMatchers("/","/register")
//                    .permitAll()
//                    .anyRequest()
//                    .authenticated()
//                    .and()
//                .formLogin()
//                    .loginPage("/login")
//                    .permitAll()
//                    .defaultSuccessUrl("/recipes")
//                    .and()
//                .logout()
//                    .logoutUrl("/logout")
//                    .permitAll();
        // @formatter:on

		// @formatter:off
        http.csrf().disable()
                .authorizeRequests()
//                    .antMatchers("/").permitAll()
                    .antMatchers("/login", "/register", "/index").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/index")
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/index")
                    .and()
                .exceptionHandling();
		
		http.headers().frameOptions().disable();
//         .antMatchers("/dashboard/**").hasAuthority("ADMIN").anyRequest()

        // @formatter:on
	}

	@Bean
	public UserDetailsService userDetailsService(UserService userService) {
		// return userService::findByUsername;

		// for debugging purposes
		return userService::findByUsername;
	}
}
