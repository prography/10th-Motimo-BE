package kr.co.api.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.api.security.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationFilterTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    private TokenAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new TokenAuthenticationFilter(tokenProvider, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("유효한 토큰이 있을 때 SecurityContext에 인증 정보가 저장된다")
    void doFilterInternal_validToken_setsAuthentication() throws Exception {
        // given
        String token = "validToken";
        UUID userId = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(userDetailsService.loadUserById(userId)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Collections.emptyList());

        // when
        filter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProvider).validateToken(token);
        verify(tokenProvider).getUserIdFromToken(token);
        verify(userDetailsService).loadUserById(userId);
        verify(filterChain).doFilter(request, response);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }

    @Test
    @DisplayName("유효하지 않은 토큰이 있을 때 인증 정보가 저장되지 않는다")
    void doFilterInternal_invalidToken_doesNotSetAuthentication() throws Exception {
        // given
        String token = "invalidToken";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenProvider.validateToken(token)).thenReturn(false);

        // when
        filter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProvider).validateToken(token);
        verify(userDetailsService, never()).loadUserById(any());
        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Authorization 헤더가 없을 때 인증 정보가 저장되지 않는다")
    void doFilterInternal_noToken_doesNotSetAuthentication() throws Exception {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);

        // when
        filter.doFilterInternal(request, response, filterChain);

        // then
        verify(tokenProvider, never()).validateToken(any());
        verify(userDetailsService, never()).loadUserById(any());
        verify(filterChain).doFilter(request, response);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}