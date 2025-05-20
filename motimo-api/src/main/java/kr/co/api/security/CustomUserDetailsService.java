package kr.co.api.security;

import kr.co.api.user.service.UserService;
import kr.co.domain.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);
        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userService.findById(id);
        return UserPrincipal.create(user);
    }

}
