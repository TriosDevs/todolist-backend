package dev.oguzhanercelik.interceptor;

import dev.oguzhanercelik.config.MdcConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class RequestHeaderAttributesHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        MDC.put(MdcConstant.X_TRACE_ID, UUID.randomUUID().toString());
        MDC.put(MdcConstant.ACCEPT_LANGUAGE, getAcceptLanguage(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        MDC.remove(MdcConstant.X_TRACE_ID);
        MDC.remove(MdcConstant.ACCEPT_LANGUAGE);
        MDC.remove(MdcConstant.X_USER_ID);
    }

    private String getAcceptLanguage(HttpServletRequest httpRequest) {
        final String acceptLanguage = httpRequest.getHeader(MdcConstant.ACCEPT_LANGUAGE);
        return StringUtils.isEmpty(acceptLanguage) ? "tr" : acceptLanguage;
    }

}