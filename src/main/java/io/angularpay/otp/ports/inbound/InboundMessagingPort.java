package io.angularpay.otp.ports.inbound;

import io.angularpay.otp.models.platform.PlatformConfigurationIdentifier;

public interface InboundMessagingPort {
    void onMessage(String message, PlatformConfigurationIdentifier identifier);
}
