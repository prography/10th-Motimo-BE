package kr.co.api.security;

import kr.co.api.user.service.UserQueryService;
import kr.co.domain.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserQueryService userQueryService;

    public CustomUserDetailsService(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userQueryService.findByEmail(username);
        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(UUID id) {
        User user = userQueryService.findById(id);
        return UserPrincipal.create(user);
    }

}
