package io.github.yohanesty.edocgateway.protocol;

import io.github.yohanesty.edocgateway.model.DummyMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyMessageCodecTest {
    @Test
    void roundTripJson() throws Exception {
        DummyMessageCodec codec = new DummyMessageCodec();
        DummyMessage message = new DummyMessage();
        message.setRequestId("DEMO-1");
        message.setMessageType("DOCUMENT_REGISTER");
        message.setTimestamp("2026-10-30T10:00:00Z");
        message.setPayload("synthetic-demo-data");
        message.setChecksum(codec.checksum(message.getPayload()));

        DummyMessage parsed = codec.parse(codec.toJson(message));
        assertEquals(message.getRequestId(), parsed.getRequestId());
        assertEquals(message.getChecksum(), parsed.getChecksum());
    }
}
