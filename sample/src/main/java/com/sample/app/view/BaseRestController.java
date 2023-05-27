package com.sample.app.view;
import com.sample.app.common.security.AuthToken;
import com.sample.app.common.security.WebServiceUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class BaseRestController {
protected WebServiceUser getUser()
{
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Objects.requireNonNull(authentication);
    if(!(authentication instanceof AuthToken)){
        throw  new IllegalStateException("Authentication token is not valid");

    }
    AuthToken token= (AuthToken)SecurityContextHolder.getContext().getAuthentication();
    return token.getUser();
}

}
