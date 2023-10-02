package io.angularpay.otp.adapters.inbound;

import io.angularpay.otp.domain.commands.*;
import io.angularpay.otp.models.*;
import io.angularpay.otp.ports.inbound.RestApiPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.angularpay.otp.helpers.Helper.fromHeaders;

@RestController
@RequestMapping("/otp/requests")
@RequiredArgsConstructor
public class RestApiAdapter implements RestApiPort {

    private final CreateOtpCommand createOtpCommand;
    private final ValidateOtpCommand validateOtpCommand;

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public CreateOtpResponse createOtp(
            @RequestBody CreateOtpAApiModel createOtpAApiModel,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        CreateOtpCommandRequest createOtpCommandRequest = CreateOtpCommandRequest.builder()
                .createOtpAApiModel(createOtpAApiModel)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.createOtpCommand.execute(createOtpCommandRequest);
    }

    @PostMapping("/validate")
    @ResponseBody
    @Override
    public ValidateOtpResponse validateOtp(
            @RequestBody ValidateOtpAApiModel validateOtpAApiModel,
            @RequestHeader Map<String, String> headers) {
        AuthenticatedUser authenticatedUser = fromHeaders(headers);
        ValidateOtpCommandRequest validateOtpCommandRequest = ValidateOtpCommandRequest.builder()
                .validateOtpAApiModel(validateOtpAApiModel)
                .authenticatedUser(authenticatedUser)
                .build();
        return this.validateOtpCommand.execute(validateOtpCommandRequest);
    }
}
