package io.github.yohanesty.edocgateway.config;

public class PartnerProfile {
    private final String partnerId;
    private final int maxPayloadBytes;
    private final boolean checksumRequired;

    public PartnerProfile(String partnerId, int maxPayloadBytes, boolean checksumRequired) {
        this.partnerId = partnerId;
        this.maxPayloadBytes = maxPayloadBytes;
        this.checksumRequired = checksumRequired;
    }

    public String getPartnerId() { return partnerId; }
    public int getMaxPayloadBytes() { return maxPayloadBytes; }
    public boolean isChecksumRequired() { return checksumRequired; }

    public static PartnerProfile demo() {
        return new PartnerProfile("DEMO_PARTNER", 1024 * 1024, true);
    }
}
