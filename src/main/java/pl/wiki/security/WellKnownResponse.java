package pl.wiki.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WellKnownResponse {
    @JsonProperty("realm")
    private String realm;
    private String public_key;
    @JsonProperty("token-service")
    private String tokenService;
    @JsonProperty("account-service")
    private String accountService;
    @JsonProperty("tokens-not-before")
    private String tokensNotBefore;

    public WellKnownResponse() {
    }

    public WellKnownResponse(String realm, String public_key, String tokenService, String accountService, String tokensNotBefore) {
        this.realm = realm;
        this.public_key = public_key;
        this.tokenService = tokenService;
        this.accountService = accountService;
        this.tokensNotBefore = tokensNotBefore;
    }

    public String getPublic_key() {
        return public_key;
    }
}