//package com.eight.sailingship.service.customer;
//
//import com.eight.sailingship.entity.RefreshEntity;
//import com.eight.sailingship.jwt.JWTUtil;
//import com.eight.sailingship.repository.RefreshRepository;
//import io.jsonwebtoken.ExpiredJwtException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class ReissueService {
//
//    private final JWTUtil jwtUtil;
//    private final RefreshRepository refreshRepository;
//
//    public ReissueService(JWTUtil jwtUtil, RefreshRepository refreshRepository) {
//        this.jwtUtil = jwtUtil;
//        this.refreshRepository = refreshRepository;
//    }
//
//    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//        String refresh = getRefreshToken(request);
//        if (refresh == null) {
//            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
//        }
//
//        try {
//            jwtUtil.isExpired(refresh);
//        } catch (ExpiredJwtException e) {
//            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
//        }
//
//        String category = jwtUtil.getCategory(refresh);
//        if (!category.equals("refresh")) {
//            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
//        }
//
//        if (!refreshRepository.existsByRefresh(refresh)) {
//            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
//        }
//
//        String email = jwtUtil.getEmail(refresh);
//        String role = jwtUtil.getRole(refresh);
//
//        String newAccess = jwtUtil.createJwt("access", email, role, 20000L);
//        String newRefresh = jwtUtil.createJwt("refresh", email, role, 86400000L);
//
//        refreshRepository.deleteByRefresh(refresh);
//        addRefreshEntity(email, newRefresh, 86400000L);
//
//        response.setHeader("access", newAccess);
//        response.addCookie(createCookie("refresh", newRefresh));
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    private String getRefreshToken(HttpServletRequest request) {
//        String refresh = null;
//        Cookie[] cookies = request.getCookies();
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("refresh")) {
//                refresh = cookie.getValue();
//                break;
//            }
//        }
//        return refresh;
//    }
//
//    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
//        Date date = new Date(System.currentTimeMillis() + expiredMs);
//
//        RefreshEntity refreshEntity = new RefreshEntity();
//        refreshEntity.setEmail(email);
//        refreshEntity.setRefresh(refresh);
//        refreshEntity.setExpiration(date.toString());
//
//        refreshRepository.save(refreshEntity);
//    }
//
//    private Cookie createCookie(String key, String value) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24 * 60 * 60);
//        cookie.setHttpOnly(true);
//        return cookie;
//    }
//}
