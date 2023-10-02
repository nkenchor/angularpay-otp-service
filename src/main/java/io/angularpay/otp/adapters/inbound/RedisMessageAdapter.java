package io.angularpay.otp.adapters.inbound;

import io.angularpay.otp.domain.commands.PlatformConfigurationsConverterCommand;
import io.angularpay.otp.models.platform.PlatformConfigurationIdentifier;
import io.angularpay.otp.ports.inbound.InboundMessagingPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.angularpay.otp.models.platform.PlatformConfigurationSource.TOPIC;

@Service
@RequiredArgsConstructor
public class RedisMessageAdapter implements InboundMessagingPort {

    private final PlatformConfigurationsConverterCommand converterCommand;

    @Override
    public void onMessage(String message, PlatformConfigurationIdentifier identifier) {
        this.converterCommand.execute(message, identifier, TOPIC);
    }
}
