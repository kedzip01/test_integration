package app.config;

import org.apache.catalina.Globals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;


@Configuration
@Profile({"dev","test","prod"})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    public AuthenticationService authenticationService;

    @Value("${server.context-path}")
    private String contextPath;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(createHandlerInterceptor());
    }

    HandlerInterceptor createHandlerInterceptor() {
        return new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                HttpSession session = httpServletRequest.getSession();
                AuthenticatedUser authenticatedUser = authenticationService.getAuthenticatedUser(session);
                Logging.setLoggingContext(session, authenticatedUser);
                if (authenticatedUser == null || !authenticatedUser.isLogged()) {
                    String requestUri = httpServletRequest.getRequestURI();
                    log.debug("Anonymous access to {} service has been intercepted", requestUri);
                    if (isAllowed(requestUri)) {
                        return true;
                    }
                    if (isRestRequest(requestUri)) {
                        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        httpServletResponse.sendRedirect(contextPath + "/login");
                    }
                    return false;
                }
                return true;
            }

            boolean isAllowed(String requestUri) {
                return
                        Pattern.matches(contextPath + "/login|" + contextPath + "/login;.*|" + contextPath + "/login\\?.*", requestUri) ||
                                Pattern.matches(contextPath + "/error|" + contextPath + "/error\\?.*", requestUri) ||
                                Pattern.matches(contextPath + "/confirm-meeting|" + contextPath + "/confirm-meeting\\?.*", requestUri) ||
                                Pattern.matches(contextPath + "/rest/device/.*", requestUri);
            }

            boolean isRestRequest(String requestUri) {
                return Pattern.matches(contextPath + "/rest/.*", requestUri);
            }

            @Override
            public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
            }

            @Override
            public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
            }
        };
    }

    @Bean
    public Filter glyphiconsFilter() {
        return new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
                final HttpServletResponse res = (HttpServletResponse) servletResponse;
                String requestObject = (String) servletRequest.getAttribute(Globals.DISPATCHER_REQUEST_PATH_ATTR);
                if (requestObject != null && isGlyphiconLibrary(requestObject)) {
                    res.setHeader("Cache-Control", "max-age=123");
                }

                filterChain.doFilter(servletRequest, servletResponse);
            }

            private boolean isGlyphiconLibrary(String requestObject) {
                return true;
            }

            @Override
            public void destroy() {
            }
        };
    }

}
