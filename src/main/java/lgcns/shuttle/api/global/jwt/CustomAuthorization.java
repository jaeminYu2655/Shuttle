package lgcns.shuttle.api.global.jwt;


import lgcns.shuttle.api.global.common.entity.Api;
import lgcns.shuttle.api.global.common.entity.ApiId;
import lgcns.shuttle.api.global.common.repository.ApiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorization implements AuthorizationManager<RequestAuthorizationContext> {
    private final ApiRepository apiRepository;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        if (!(authentication.get().getPrincipal() instanceof UserDetails userDetails)) {
            throw new AuthorizationServiceException("Cannot check authorization without UserDetails");
        }

        Collection<? extends GrantedAuthority> authorityCollection = userDetails.getAuthorities();
        for(GrantedAuthority authority : authorityCollection) {
            ApiId apiId = new ApiId(object.getRequest().getRequestURI(), authority.getAuthority());
            Optional<Api> opt = apiRepository.findById(apiId);
            Api api = opt.orElse(null);

            if (api != null && api.getYN().equals("Y"))
                return new AuthorizationDecision(true);
        }
        throw new AuthorizationServiceException("Cannot check authorization");
    }
}