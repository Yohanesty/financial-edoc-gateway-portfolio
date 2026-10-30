package io.github.yohanesty.edocgateway.service;

import io.github.yohanesty.edocgateway.config.PartnerProfile;
import io.github.yohanesty.edocgateway.model.DummyMessage;
import io.github.yohanesty.edocgateway.protocol.DummyMessageCodec;

import java.nio.charset.StandardCharsets;

public class MessageValidator {
    private final PartnerProfile profile;
    private final DummyMessageCodec codec;

    public MessageValidator(PartnerProfile profile, DummyMessageCodec codec) {
        this.profile = profile;
        this.codec = codec;
    }

    public void validate(DummyMessage message) throws Exception {
        if (blank(message.getRequestId())) {
            throw new IllegalArgumentException("requestId is required");
        }
        if (!"DOCUMENT_REGISTER".equals(message.getMessageType())) {
            throw new IllegalArgumentException("unsupported messageType");
        }

        byte[] payload = (message.getPayload() == null ? "" : message.getPayload())
                .getBytes(StandardCharsets.UTF_8);
        if (payload.length > profile.getMaxPayloadBytes()) {
            throw new IllegalArgumentException("payload too large");
        }

        if (profile.isChecksumRequired()) {
            String expected = codec.checksum(message.getPayload());
            if (!expected.equalsIgnoreCase(message.getChecksum())) {
                throw new IllegalArgumentException("checksum mismatch");
            }
        }
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
