package io.github.yohanesty.edocgateway.model;

import java.time.Instant;
import java.util.UUID;

public class ErrorRecord {
    private final String id = UUID.randomUUID().toString();
    private final String partnerId;
    private final String rawMessage;
    private final String reason;
    private final Instant createdAt = Instant.now();

    public ErrorRecord(String partnerId, String rawMessage, String reason) {
        this.partnerId = partnerId;
        this.rawMessage = rawMessage;
        this.reason = reason;
    }

    public String getId() { return id; }
    public String getPartnerId() { return partnerId; }
    public String getRawMessage() { return rawMessage; }
    public String getReason() { return reason; }
    public Instant getCreatedAt() { return createdAt; }
}
