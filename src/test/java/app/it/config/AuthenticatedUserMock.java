package app.it.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Configuration
@Profile("integration-test")
@Ignore
public class AuthenticatedUserMock {

    @Value("${integrationtest.test.auth.username}")
    protected String userName;
    @Value("${integrationtest.test.auth.encryptionpassword}")
    protected String password;
    @Value("${integrationtest.test.auth.email}")
    protected String email;
    @Value("${integrationtest.test.auth.token}")
    protected String token;
    @Value("${integrationtest.test.auth.role}")
    protected String role;
    @Value("${integrationtest.test.auth.salt}")
    protected String salt;

    @Bean
    public AuthenticatedUser authenticatedUser() {
        final AuthenticatedUser authenticatedUser = new AuthenticatedUser(salt, password, email, userName);
        authenticatedUser.setToken(token);
        authenticatedUser.setUserRoles(Collections.singletonList(UserRoleEnum.SYSTEM_ADMIN));
        return authenticatedUser;
    }
}
