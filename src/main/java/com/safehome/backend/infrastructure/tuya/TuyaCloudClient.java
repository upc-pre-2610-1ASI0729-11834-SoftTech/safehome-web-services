package com.safehome.backend.infrastructure.tuya;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class TuyaCloudClient {

    @Value("${tuya.access-id:}")
    private String accessId;

    @Value("${tuya.access-secret:}")
    private String accessSecret;

    @Value("${tuya.endpoint:https://openapi.tuyaus.com}")
    private String endpoint;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    private String cachedAccessToken;
    private long tokenExpirationTime = 0;

    public List<TuyaDeviceProperty> getDeviceProperties(String deviceId) {
        try {
            String accessToken = getAccessToken();

            String path = "/v2.0/cloud/thing/" + deviceId + "/shadow/properties";
            String response = sendGet(path, accessToken);

            JsonNode root = objectMapper.readTree(response);

            if (!root.path("success").asBoolean(false)) {
                throw new RuntimeException("Tuya API error: " + response);
            }

            JsonNode propertiesNode = root.path("result").path("properties");
            List<TuyaDeviceProperty> properties = new ArrayList<>();

            if (propertiesNode.isArray()) {
                for (JsonNode propertyNode : propertiesNode) {
                    properties.add(new TuyaDeviceProperty(
                            propertyNode.path("code").asText(),
                            propertyNode.path("value").asText(),
                            propertyNode.path("type").asText()
                    ));
                }
            }

            return properties;

        } catch (Exception e) {
            throw new RuntimeException("Could not get Tuya device properties", e);
        }
    }

    private String getAccessToken() {
        long now = Instant.now().getEpochSecond();

        if (cachedAccessToken != null && now < tokenExpirationTime) {
            return cachedAccessToken;
        }

        try {
            String path = "/v1.0/token?grant_type=1";
            String response = sendGetWithoutToken(path);

            JsonNode root = objectMapper.readTree(response);

            if (!root.path("success").asBoolean(false)) {
                throw new RuntimeException("Could not get Tuya access token: " + response);
            }

            JsonNode result = root.path("result");
            cachedAccessToken = result.path("access_token").asText();

            int expireTime = result.path("expire_time").asInt(3600);
            tokenExpirationTime = now + expireTime - 60;

            return cachedAccessToken;

        } catch (Exception e) {
            throw new RuntimeException("Could not get Tuya access token", e);
        }
    }

    private String sendGetWithoutToken(String path) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String stringToSign = buildStringToSign("GET", path);
        String sign = hmacSha256(accessId + timestamp + stringToSign, accessSecret);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + path))
                .GET()
                .header("client_id", accessId)
                .header("sign", sign)
                .header("t", timestamp)
                .header("sign_method", "HMAC-SHA256")
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private String sendGet(String path, String accessToken) throws Exception {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String stringToSign = buildStringToSign("GET", path);
        String sign = hmacSha256(accessId + accessToken + timestamp + stringToSign, accessSecret);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + path))
                .GET()
                .header("client_id", accessId)
                .header("access_token", accessToken)
                .header("sign", sign)
                .header("t", timestamp)
                .header("sign_method", "HMAC-SHA256")
                .header("Content-Type", "application/json")
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private String buildStringToSign(String method, String path) throws Exception {
        String emptyBodyHash = sha256Hex("");
        return method + "\n" +
                emptyBodyHash + "\n" +
                "\n" +
                path;
    }

    private String sha256Hex(String value) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private String hmacSha256(String data, String secret) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        return bytesToHex(sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8))).toUpperCase(Locale.ROOT);
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    public record TuyaDeviceProperty(
            String code,
            String value,
            String type
    ) {
    }
}