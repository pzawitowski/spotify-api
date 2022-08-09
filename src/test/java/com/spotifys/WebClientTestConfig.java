package com.spotifys;

import okhttp3.mockwebserver.MockWebServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@TestConfiguration
public class WebClientTestConfig {

    @Bean
    public MockWebServer mockWebServer() {
        return new MockWebServer();
    }

    @Bean
    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager, MockWebServer mockWebServer) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction filter = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientManager);
        filter.setDefaultClientRegistrationId("spotifyclient");
        return WebClient.builder().apply(filter.oauth2Configuration()).baseUrl(
                 String.format("http://localhost:%s%s", mockWebServer.getPort(), "/v1")
        ).build();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(
                clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }

    @Bean
    public ClientRegistrationRepository registrationRepository(MockWebServer mockWebServer) {
        ClientRegistration registration = ClientRegistrations
                .fromIssuerLocation(String.format("http://localhost:%s", mockWebServer.getPort()))
                .tokenUri(String.format("http://localhost:%s/%s", mockWebServer.getPort(), "token"))
                .build();
        return new InMemoryClientRegistrationRepository(registration);
    }
}
