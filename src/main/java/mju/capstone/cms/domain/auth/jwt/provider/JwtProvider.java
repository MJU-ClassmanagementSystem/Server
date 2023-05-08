package mju.capstone.cms.domain.auth.jwt.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;


@Slf4j
public class JwtProvider implements InitializingBean {

    //jwt에 생성에 사용되는 secret key
    @Value("${jwt.secret_key}")
    private String secretKey;

    //jwt expired 시간
    @Value("${jwt.expiration}")
    private Long expiration;

    //request header의 Authorization
    @Value("${jwt.header}")
    private String header;

    // hmacSha 알고리즘에 생성되는 키.
    private Key key;

    // secretKey decoding, 키 생성
    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] decode = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(decode);
    }

    /**
     * token 생성
     * type으로 교사/학부모 구분 teacher -> type="teacher", parent -> type="parent"
     * sub 클레임에 id
     */
    public String createToken(String id, String type) {
        Date now = new Date();
        return Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setSubject(id)
            .claim("type", type)
            .setExpiration(new Date(now.getTime() + expiration))
            .compact();
    }

    /**
     * bearerToken 으로 부터 token 값만 추출.
     */
    public String extractToken(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[1];
        }
        return null;
    }

    /**
     * token 으로부터 id(sub) 추출
     */
    public String extractId(String bearerToken) {
        String token = extractToken(bearerToken);
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    /**
     * token 으로부터 type 추출
     */
    public String extractType(String bearerToken) {
        String token = extractToken(bearerToken);
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("type", String.class);
    }

    /**
     * token validation
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException exception) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 입니다.");
        }catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 입니다.");
        }catch (IllegalArgumentException e) {
            log.info("잘못된 JWT 입니다.");
        }
        return false;
    }

}
