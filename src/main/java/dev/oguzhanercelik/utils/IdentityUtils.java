package dev.oguzhanercelik.utils;

import dev.oguzhanercelik.model.IdentityUser;
import dev.oguzhanercelik.model.UserAuthentication;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdentityUtils {

    public static IdentityUser getUser() {
        final UserAuthentication userAuthentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return userAuthentication.getDetails();
    }

}