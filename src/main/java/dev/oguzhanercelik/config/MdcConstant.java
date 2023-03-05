package dev.oguzhanercelik.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MdcConstant {

    // you can change or add
    public static final String X_TRACE_ID = "x-trace-id";
    public static final String X_USER_ID = "x-user-id";
    public static final String ACCEPT_LANGUAGE = "accept-language";

}
