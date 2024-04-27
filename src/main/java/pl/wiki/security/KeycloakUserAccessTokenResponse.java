package pl.wiki.security;

public class KeycloakUserAccessTokenResponse extends KeycloakAccessTokenResponse {
    private String refresh_token;
    private String session_state;
    private String id_token;


    public KeycloakUserAccessTokenResponse() {}

    public KeycloakUserAccessTokenResponse(String accessToken, int expires_in, int refresh_expires_in, String token_type, int not_before_policy, String scope, String refresh_token, String session_state, String id_token) {
        super(accessToken, expires_in, refresh_expires_in, token_type, not_before_policy, scope);
        this.refresh_token = refresh_token;
        this.session_state = session_state;
        this.id_token = id_token;
    }

    @Override
    public String toString() {
        return "KeycloakUserAccessTokenResponse{" +
                "refresh_token='" + refresh_token + '\'' +
                ", session_state='" + session_state + '\'' +
                ", id_token='" + id_token + '\'' +
                ", access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_expires_in=" + refresh_expires_in +
                ", token_type='" + token_type + '\'' +
                ", not_before_policy=" + not_before_policy +
                ", scope='" + scope + '\'' +
                '}';
    }

    public String getId_token() {
        return id_token;
    }
}