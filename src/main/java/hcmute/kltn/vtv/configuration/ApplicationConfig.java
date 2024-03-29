package hcmute.kltn.vtv.configuration;

import hcmute.kltn.vtv.authentication.request.RegisterRequest;
import hcmute.kltn.vtv.authentication.service.IAuthenticationService;
import hcmute.kltn.vtv.repository.user.CustomerRepository;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final CustomerRepository customerRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> customerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Tài khoản không tồn tại. Vui lòng thử lại."));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailsService());
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(
//            IAuthenticationService service) {
//        Date currentDate = new Date();
//        return args -> {
//            var us1 = RegisterRequest.builder()
//                    .username("string")
//                    .fullName("To Duy Vuong")
//                    .email("conc5288@gmail.com")
//                    .password("string")
//                    .birthday(currentDate)
//                    .gender(true)
//                    .build();
//            System.out.println("user1 token: " + service.register(us1));
//
//        };
//    }

    // @Bean
    // public CommandLineRunner commandLineRunner2(
    // IAdminService service
    // ) {
    // Date currentDate = new Date();
    // return args -> {
    //
    // var ad1 = RegisterRequest.builder()
    // .username("ad")
    // .fullName("Adưv")
    // .email("Ad288@gmail.com")
    // .password("string")
    // .birthday(currentDate)
    // .gender(false)
    // .build();
    // System.out.println("ad token: " + service.register(ad1));
    //
    //
    //
    //
    //
    // };
    // }

    public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

        public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
            setUserDetailsService(userDetailsService);
            setPasswordEncoder(passwordEncoder());
        }

        @Override
        protected void additionalAuthenticationChecks(UserDetails userDetails,
                UsernamePasswordAuthenticationToken authentication) {
            if (!passwordEncoder().matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
                throw new BadRequestException("Sai mật khẩu. Vui lòng thử lại.");
            }
        }
    }
}
