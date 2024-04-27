package pl.wiki.security;

class KeycloakAccessTokenResponse {
    protected String access_token;
    protected int expires_in;
    protected int refresh_expires_in;
    protected String token_type;
    protected int not_before_policy;
    protected String scope;

    public KeycloakAccessTokenResponse() {
    }

    public KeycloakAccessTokenResponse(String accessToken, int expires_in, int refresh_expires_in, String token_type, int not_before_policy, String scope) {
        this.access_token = accessToken;
        this.expires_in = expires_in;
        this.refresh_expires_in = refresh_expires_in;
        this.token_type = token_type;
        this.not_before_policy = not_before_policy;
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "AccessTokenResponse{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_expires_in=" + refresh_expires_in +
                ", token_type='" + token_type + '\'' +
                ", not_before_policy=" + not_before_policy +
                ", scope='" + scope + '\'' +
                '}';
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }
}