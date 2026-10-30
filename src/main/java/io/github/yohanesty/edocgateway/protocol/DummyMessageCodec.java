package io.github.yohanesty.edocgateway.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yohanesty.edocgateway.model.DummyMessage;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class DummyMessageCodec {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DummyMessage parse(String json) throws Exception {
        return objectMapper.readValue(json, DummyMessage.class);
    }

    public String toJson(DummyMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }

    public String checksum(String payload) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest((payload == null ? "" : payload).getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
