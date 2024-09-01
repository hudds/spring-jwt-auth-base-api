package dev.hudsonprojects.simplechat.security;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SuccessfulAuthenticationResponse  {

	@JsonProperty("access_token")
	private final String jwtToken;
	@JsonProperty("refresh_token")
	private final String refreshToken;
	@JsonProperty("expires_in")
	private final Long expiresIn;
	@JsonProperty("token_type")
	private final String tokenType;
	
	private SuccessfulAuthenticationResponse(SuccessfulAuthenticationResponse.Builder builder) {
		this.jwtToken = builder.jwtToken;
		this.refreshToken = builder.refreshToken;
		this.expiresIn = builder.expiresIn;
		this.tokenType = builder.tokenType;
	}
	
	
	public String getJwtToken() {
		return jwtToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}
	
		
	public Long getExpiresIn() {
		return expiresIn;
	}


	public String getTokenType() {
		return tokenType;
	}


	@Override
	public int hashCode() {
		return Objects.hash(expiresIn, jwtToken, refreshToken, tokenType);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuccessfulAuthenticationResponse other = (SuccessfulAuthenticationResponse) obj;
		return Objects.equals(expiresIn, other.expiresIn) && Objects.equals(jwtToken, other.jwtToken)
				&& Objects.equals(refreshToken, other.refreshToken) && Objects.equals(tokenType, other.tokenType);
	}



	@Override
	public String toString() {
		return "SuccessfulAuthenticationResponse [jwtToken=" + jwtToken + ", refreshToken=" + refreshToken
				+ ", expiresIn=" + expiresIn + ", tokenType=" + tokenType + "]";
	}



	public static class Builder{
		private String jwtToken;
		private String refreshToken;
		private String tokenType;
		private Long expiresIn;
		
		public Builder setJwtToken(String jwtToken) {
			this.jwtToken = jwtToken;
			return this;
		}
		
		public Builder setRefreshToken(String refreshToken) {
			this.refreshToken = refreshToken;
			return this;
		}
		
		public Builder setTokenType(String tokenType) {
			this.tokenType = tokenType;
			return this;
		}
		
		public Builder setExpiresIn(Long expiresIn) {
			this.expiresIn = expiresIn;
			return this;
		}
		
		public SuccessfulAuthenticationResponse build() {
			return new SuccessfulAuthenticationResponse(this);
		}
		
	}
}

