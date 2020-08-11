package az.gdg.msteam.filter;

import az.gdg.msteam.client.AuthenticationClient;
import az.gdg.msteam.model.client.auth.UserInfo;
import az.gdg.msteam.security.MemberAuthentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static az.gdg.msteam.model.client.auth.HttpHeader.X_AUTH_TOKEN;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {
    private AuthenticationClient authenticationClient;

    @Autowired
    public void setAuthenticationClient(AuthenticationClient authenticationClient) {
        this.authenticationClient = authenticationClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String authToken = request.getHeader(X_AUTH_TOKEN);
            if (authToken != null) {
                UserInfo userInfo = authenticationClient.validateToken(authToken);
                if (userInfo == null) {
                    throw new RuntimeException("User info is not valid");
                } else {
                    MemberAuthentication memberAuthentication = new MemberAuthentication(userInfo.getRole(),
                            true);
                    SecurityContextHolder.getContext().setAuthentication(memberAuthentication);
                }
            }
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
