package kr.co.api.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Component
public class CustomOAuth2AuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final String DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME =
            HttpSessionOAuth2AuthorizationRequestRepository.class.getName()
                    + ".AUTHORIZATION_REQUEST";

    private static final String REDIRECT_URI_PARAM = "redirect_uri_param";

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");
        return getAuthorizationRequest(request);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        if (authorizationRequest == null) {
            removeAuthorizationRequest(request, response);
            return;
        }

        String state = authorizationRequest.getState();
        Assert.hasText(state, "authorizationRequest.state cannot be empty");

        HttpSession session = request.getSession();
        session.setAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME, authorizationRequest);

        String redirectUriParam = request.getParameter("redirect_uri");
        if (StringUtils.hasText(redirectUriParam)) {
            session.setAttribute(REDIRECT_URI_PARAM + state, redirectUriParam);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
            HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        OAuth2AuthorizationRequest authorizationRequest = getAuthorizationRequest(request);

        if (authorizationRequest != null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME);
            }
        }

        return authorizationRequest;
    }

    public String getAndRemoveRedirectUriParam(HttpServletRequest request) {
        String state = request.getParameter("state");
        HttpSession session = request.getSession(false);
        if (session != null) {
            String redirectUriParam = (String) session.getAttribute(REDIRECT_URI_PARAM + state);
            if (redirectUriParam != null) {
                session.removeAttribute(REDIRECT_URI_PARAM + state);
                return redirectUriParam;
            }
        }
        return null;
    }

    private OAuth2AuthorizationRequest getAuthorizationRequest(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) ? (OAuth2AuthorizationRequest) session.getAttribute(
                DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME) : null;
    }
}