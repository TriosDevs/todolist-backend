package dev.oguzhanercelik.filter;

import dev.oguzhanercelik.exception.AuthenticationFailedException;
import dev.oguzhanercelik.service.AuthenticationService;
import dev.oguzhanercelik.utils.IdentityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static dev.oguzhanercelik.config.MdcConstant.X_USER_ID;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationTokenFilter extends GenericFilterBean {

    private final AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        try {
            Authentication userAuthentication = authenticationService.getUserAuthentication(httpRequest);
            SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            MDC.put(X_USER_ID, IdentityUtils.getUser().getId());
        } catch (AuthenticationFailedException e) {
            handleSecurityError((HttpServletResponse) servletResponse, "Authentication header not valid.");
            return;
        } catch (MalformedJwtException m) {
            handleSecurityError((HttpServletResponse) servletResponse, "JWT was not correctly constructed.");
            return;
        } catch (ExpiredJwtException e) {
            handleSecurityError((HttpServletResponse) servletResponse, "Token is expired.");
            return;
        } catch (SignatureException e) {
            handleSecurityError((HttpServletResponse) servletResponse, "Token is not valid.");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void handleSecurityError(HttpServletResponse httpServletResponse, String message) throws IOException {
        SecurityContextHolder.clearContext();
        httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), message);
    }
}