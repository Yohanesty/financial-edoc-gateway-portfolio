package io.github.yohanesty.edocgateway.protocol;

import io.github.yohanesty.edocgateway.config.PartnerProfile;
import io.github.yohanesty.edocgateway.model.DummyMessage;
import io.github.yohanesty.edocgateway.service.MessageValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageValidatorTest {
    @Test
    void acceptsValidSyntheticMessage() throws Exception {
        DummyMessageCodec codec = new DummyMessageCodec();
        MessageValidator validator = new MessageValidator(PartnerProfile.demo(), codec);
        DummyMessage message = message(codec, "synthetic-demo-data");

        assertDoesNotThrow(() -> validator.validate(message));
    }

    @Test
    void rejectsChecksumMismatch() throws Exception {
        DummyMessageCodec codec = new DummyMessageCodec();
        MessageValidator validator = new MessageValidator(PartnerProfile.demo(), codec);
        DummyMessage message = message(codec, "synthetic-demo-data");
        message.setChecksum("invalid-checksum");

        assertThrows(IllegalArgumentException.class, () -> validator.validate(message));
    }

    private DummyMessage message(DummyMessageCodec codec, String payload) throws Exception {
        DummyMessage message = new DummyMessage();
        message.setRequestId("DEMO-VALIDATION-1");
        message.setMessageType("DOCUMENT_REGISTER");
        message.setTimestamp("2026-10-30T10:00:00Z");
        message.setPayload(payload);
        message.setChecksum(codec.checksum(payload));
        return message;
    }
}
