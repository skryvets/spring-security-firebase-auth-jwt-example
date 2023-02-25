package com.skryvets.springfirebasedemo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class AppController {

    @Value("${firebase.project-key}")
    private String firebaseProjectKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String FIREBASE_BASE_URL = "https://identitytoolkit.googleapis.com/v1/accounts";
    private final String FIREBASE_SIGN_IN_URL = ":signInWithPassword";
    private final String FIREBASE_SIGN_UP_URL = ":signUp";
    private final String FIREBASE_KEY_PARAM = "?key=";

    //Private endpoint. Only accessible with token.
    @GetMapping("/private")
    public Map<String, String> privateEndpoint() {
        return Collections.singletonMap("status", "ok");
    }

    //Public endpoint
    @GetMapping("/public")
    public Map<String, String> publicEndpoint() {
        return Collections.singletonMap("status", "ok");
    }

    //Register user with email and password
    @PostMapping("/register")
    public TokenResponse register(@RequestBody Credentials credentials) {
        return restTemplate.postForObject(
            FIREBASE_BASE_URL + FIREBASE_SIGN_UP_URL + FIREBASE_KEY_PARAM + firebaseProjectKey,
            credentials,
            TokenResponse.class);
    }

    //Retrieve token from firebase
    @PostMapping("/token/fetch")
    public TokenResponse fetchToken(@RequestBody Credentials credentials) {
        return restTemplate.postForObject(
            FIREBASE_BASE_URL + FIREBASE_SIGN_IN_URL + FIREBASE_KEY_PARAM + firebaseProjectKey,
            credentials,
            TokenResponse.class);
    }

    @PostMapping("/token/refresh")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest request) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type","refresh_token");
        map.add("refresh_token", request.refreshToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

        return restTemplate.exchange(
                "https://securetoken.googleapis.com/v1/token" + FIREBASE_KEY_PARAM + firebaseProjectKey,
                HttpMethod.POST,
                entity,
                RefreshTokenResponse.class)
            .getBody();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Credentials {
        private String email;
        private String password;
        private final boolean returnSecureToken = true;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshTokenRequest {
        @JsonProperty("refresh_token")
        private String refreshToken;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenResponse {
        private String kind;
        private String localId;
        private String email;
        private String displayName;
        private String idToken;
        private Boolean registered;
        private String refreshToken;
        private String expiresIn;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RefreshTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("expires_in")
        private String expiresIn;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("id_token")
        private String idToken;
        @JsonProperty("user_id")
        private String userId;
        @JsonProperty("722272864890")
        private String projectId;
    }
}
