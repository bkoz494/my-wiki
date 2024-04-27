package pl.wiki.security;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;

@Service
public class KeycloakApiService {

    private String accessToken;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String CLIENT_SECRET;
    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer_uri;

    private String setAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("client_id", CLIENT_ID);
        map.add("grant_type", "client_credentials");
        map.add("client_secret", CLIENT_SECRET);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        String uri = issuer_uri + "/protocol/openid-connect/token";
        AccessTokenResponse response = restTemplate.postForObject(uri, request, AccessTokenResponse.class);

        this.accessToken = response.getAccess_token();
        return this.accessToken;
    }

    private ResponseEntity<String> makeRequest(String role) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String accessToken = this.accessToken != null ? this.accessToken : setAccessToken();
//        System.out.println("access Token: " + accessToken);
        Assert.notNull(accessToken, "access token does not exist");
        headers.setBearerAuth(accessToken);
        HttpEntity request = new HttpEntity<>(headers);
        String resourceUrl = "http://localhost:8081/admin/realms/my-auth/roles/" + role + "/users";
        return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
    }
    public String getUsersByRole(String role) throws Exception {
        ResponseEntity<String> response = makeRequest(role);
        if(response.getStatusCode().is2xxSuccessful()){
            String responseBody = response.getBody();
//            System.out.println("Response body:\n" + responseBody);
            return responseBody;
        } else if(response.getStatusCode().is4xxClientError()) {
            setAccessToken();
            return makeRequest(role).getBody();
        }
        else {
            throw new Exception("Error while connecting to Keycloak API");
        }
    }

    private ResponseEntity<String> getUserByIdRequest(String id){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String accessToken = this.accessToken != null ? this.accessToken : setAccessToken();
        Assert.notNull(accessToken, "access token does not exist");
        headers.setBearerAuth(accessToken);
        HttpEntity request = new HttpEntity<>(headers);
        String resourceUrl = "http://localhost:8081/admin/realms/my-realm/users/" + id;
        return restTemplate.exchange(resourceUrl, HttpMethod.GET, request, String.class);
    }

    public UserDTO getUserById(String id) throws Exception {
        ResponseEntity<String> response = getUserByIdRequest(id);
        String responseBody;
        if(response.getStatusCode().is2xxSuccessful()){
            responseBody = response.getBody();
//            System.out.println("Response body:\n" + responseBody);
        } else if(response.getStatusCode().is4xxClientError()) {
            setAccessToken();
            responseBody = makeRequest(id).getBody();
        }
        else {
            throw new Exception("Error while connecting to Keycloak API");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UserDTO userDTO = objectMapper.readValue(responseBody, UserDTO.class);
        return userDTO;
    }

    public OidcIdToken retrieveIdToken(String username, String password) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);
        map.add("client_id", CLIENT_ID);
        map.add("client_secret", CLIENT_SECRET);
        map.add("scope", "openid");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        RestTemplate restTemplate = new RestTemplate();
        KeycloakUserAccessTokenResponse response = restTemplate.postForObject(
                issuer_uri + "/protocol/openid-connect/token",
                request,
                KeycloakUserAccessTokenResponse.class);
        if (response != null && response.getId_token() != null) {
            Claims claims;
            claims = Jwts
                    .parser()
                    .verifyWith(getPublicKey())
                    .build()
                    .parseSignedClaims(response.getId_token())
                    .getPayload();
            Long expiresInEpochSeconds = Instant.now().getEpochSecond() + response.getExpires_in();
            return new OidcIdToken(
                    response.getId_token(),
                    Instant.now(),
                    Instant.ofEpochSecond(expiresInEpochSeconds),
                    claims);
        } else {
            throw new Exception("Błąd połączenia z Keycloak - nie można uzyskać nowego access tokena");
        }
    }

    private PublicKey getPublicKey() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(issuer_uri, HttpMethod.GET, request, String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            ObjectMapper objectMapper = new ObjectMapper();
            String responseBody = response.getBody();
            WellKnownResponse wellKnownResponse = objectMapper.readValue(responseBody, WellKnownResponse.class);
            String publicKeyString = wellKnownResponse.getPublic_key();
            X509EncodedKeySpec spec = new X509EncodedKeySpec(Decoders.BASE64.decode(publicKeyString));
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(spec);
            return publicKey;
        } else {
            throw new Exception("cannot retrieve well known info");
        }
    }
}