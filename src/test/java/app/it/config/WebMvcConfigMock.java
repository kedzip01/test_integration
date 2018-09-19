package app.it.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Configuration
@Import({AuthenticatedUserMock.class})
@Profile("integration-test")
public class WebMvcConfigMock extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthenticatedUser authenticatedUser;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createHandlerInterceptor());
    }

    HandlerInterceptor createHandlerInterceptor() {
        return new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                HttpSession session = httpServletRequest.getSession();
                session.setAttribute(Constants.AUTHENTICATED_USER_SESSION_OBJECT_NAME,authenticatedUser);
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
            }

            @Override
            public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            }
        };
    }
}
