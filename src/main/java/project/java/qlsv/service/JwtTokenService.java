package project.java.qlsv.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtTokenService {
    //de tao jwttoken, can 1 khoa bao mat, va thoi gian het han


    @Value("${jwt.secret:123}")
    private String secretKey; // = "123"; thay vi fix cung du lieu nhu nay, ta lay du lieu tu application
    private long validity = 5; // 5 phut

    // muon luu gi vao token thi truyen vao day
    public String createToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
//        claims.put(username, claims); // muoon tao them
        Date now = new Date();
        Date exp = new Date(now.getTime() + validity * 60 * 1000); // tgian het han = now + validity

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // ham kiem tra xem token con hieu luc hay ko
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token);
            return true; //token van con hieu luc
        } catch (Exception e) {
            // do nothing - dong nghia voi viec token ko dung
        }
        return false;
    }

    // lay thong tin token
    public String getUsername(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            // do nothing
            e.printStackTrace();
        }
        return null;
    }
}
