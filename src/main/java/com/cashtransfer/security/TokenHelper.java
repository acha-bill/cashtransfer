package com.cashtransfer.security;

import com.cashtransfer.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenHelper {

	@Value("${jwt.secret}")
	public String secret;
	@Value("${app.name}")
	private String appName;
	@Value("${jwt.expires_in}")
	private int expiresIn;

	@Value("${jwt.header}")
	private String authHeader;


	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}

	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			audience = claims.getAudience();
		} catch (Exception e) {
			audience = null;
		}
		return audience;
	}

	public String refreshToken(String token) {
		String refreshedToken;
		Date a = new Date();
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			claims.setIssuedAt(a);
			refreshedToken = Jwts.builder()
					.setClaims(claims)
					.setExpiration(generateExpirationDate())
					.signWith(SIGNATURE_ALGORITHM, secret)
					.compact();
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	public String generateToken(String username) {
		return Jwts.builder()
				.setIssuer(appName)
				.setSubject(username)
				.setAudience("mobile")
				.setIssuedAt(new Date())
				.setExpiration(generateExpirationDate())
				.signWith(SIGNATURE_ALGORITHM, secret)
				.compact();
	}

	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	private Date generateExpirationDate() {
		return new Date(new Date().getTime() + expiresIn * 1000);
	}

	public int getExpiredIn() {
		return expiresIn;
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		User user = (User) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);
		return (
				username != null &&
						username.equals(userDetails.getUsername()) &&
						!isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
		);
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	public String getToken(HttpServletRequest request) {
		/**
		 *  Getting the token from Authentication header
		 *  e.g Bearer your_token
		 */
		String authHeader = getAuthHeaderFromHeader(request);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(authHeader);
	}

}