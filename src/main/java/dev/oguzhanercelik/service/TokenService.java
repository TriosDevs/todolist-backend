package dev.oguzhanercelik.service;

import dev.oguzhanercelik.config.security.JwtSecurityTokenConfiguration;
import dev.oguzhanercelik.exception.AuthenticationFailedException;
import dev.oguzhanercelik.model.dto.UserDto;
import dev.oguzhanercelik.model.error.ErrorDetail;
import dev.oguzhanercelik.model.error.ErrorEnum;
import dev.oguzhanercelik.model.error.ErrorResponse;
import dev.oguzhanercelik.utils.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    public static final String BEARER = "Bearer ";
    private final JwtSecurityTokenConfiguration jwtSecurityTokenConfiguration;
    private final JwtParser jwtParser;
    private static final int TOKEN_EXPIRATION_HOURS_COUNT = 9;

    @SneakyThrows
    public String createToken(UserDto userCreateDto) {
        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("email", userCreateDto.getEmail());
        tokenData.put("id", userCreateDto.getId());
        final LocalDateTime now = LocalDateTime.now();

        return BEARER + Jwts.builder()
                .addClaims(tokenData)
                .setExpiration(DateUtils.convertLocalDateTimeToDate(now.plusHours(TOKEN_EXPIRATION_HOURS_COUNT)))
                .setIssuedAt(DateUtils.convertLocalDateTimeToDate(now))
                .compressWith(CompressionCodecs.GZIP)
                .signWith(SignatureAlgorithm.HS512, jwtSecurityTokenConfiguration.getSecretKeySpec())
                .compact();
    }

    public Claims getTokenClaims(HttpServletRequest httpServletRequest) {
        return jwtParser.getClaims(getTokenFromHeader(httpServletRequest));
    }

    private String getTokenFromHeader(HttpServletRequest httpServletRequest) {
        String authenticationHeader = httpServletRequest.getHeader("Authorization");
        boolean startWithBearer = StringUtils.startsWith(authenticationHeader, "Bearer");
        String[] headerParams = StringUtils.split(authenticationHeader, StringUtils.SPACE);
        boolean headerParamSizeIsTwo = ArrayUtils.getLength(headerParams) == 2;
        boolean isAuthenticationHeaderValid = authenticationHeader != null && startWithBearer && headerParamSizeIsTwo;
        if (!isAuthenticationHeaderValid) {
            final ErrorDetail errorDetail = new ErrorDetail(ErrorEnum.UNAUTHORIZED.getCode(), "Authentication header not valid");
            throw new AuthenticationFailedException(ErrorResponse.builder().error(errorDetail).build());
        }
        return authenticationHeader.split(StringUtils.SPACE)[1];
    }
}