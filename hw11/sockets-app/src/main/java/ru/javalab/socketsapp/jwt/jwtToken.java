package ru.javalab.socketsapp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class jwtToken {

    public static String createToken(String id, String role){
        Algorithm algorithm = Algorithm.HMAC256("javalab");
        String token = JWT.create()
                .withIssuer(id)
                .withClaim("role", role)
                .sign(algorithm);
        return token;
    }

    public static boolean checkRole(String token){
        Algorithm algorithm = Algorithm.HMAC256("javalab");
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        if(jwt.getClaim("role").asString().equals("admin"))
            return true;
        else
            return false;
    }

    public static int getIdFromJwt(String token){
        Algorithm algorithm = Algorithm.HMAC256("javalab");
        JWTVerifier verifier = JWT.require(algorithm)
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(token);
        return Integer.parseInt(jwt.getIssuer());
    }
}
