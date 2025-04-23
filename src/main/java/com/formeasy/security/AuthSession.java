package com.formeasy.security;

import java.util.Map;
import com.formeasy.domain.Usuario;

public class AuthSession {

    private static String userLogin;
    private static String userPassword;
    private static Usuario usuario;

    private static String googleAccessToken;
    private static Map<String, String> googleUserAttributes;

    public static boolean isAuthenticated() {
        return googleAccessToken != null && !googleAccessToken.isEmpty();
    }

    public static void logout() {
        System.out.println("Realizando logout...");
        userLogin = null;
        userPassword = null;
        usuario = null;
        googleAccessToken = null;
        googleUserAttributes = null;
    }

    public static void setUserCredentials(String login, String password) {
        userLogin = login;
        userPassword = password;
    }

    public static String getUserLogin() {
        return userLogin;
    }

    public static void setUserLogin(String login) {
        userLogin = login;
    }

    public static String getUserPassword() {
        return userPassword;
    }

    public static void setUserPassword(String password) {
        userPassword = password;
    }

    public static Usuario getUser() {
        return usuario;
    }

    public static void setUser(Usuario usuario) {
        AuthSession.usuario = usuario;
    }

    // ===== Google OAuth2 =====

    public static void setGoogleAccessToken(String token) {
        googleAccessToken = token;
    }

    public static String getGoogleAccessToken() {
        return googleAccessToken;
    }

    public static void setGoogleUserAttributes(Map<String, String> attributes) {
        googleUserAttributes = attributes;
    }

    public static Map<String, String> getGoogleUserAttributes() {
        return googleUserAttributes;
    }

    public static String getGoogleUserEmail() {
        return googleUserAttributes != null ? googleUserAttributes.get("email") : null;
    }

}
