package io.angularpay.otp.ports.inbound;

import io.angularpay.otp.models.CreateOtpAApiModel;
import io.angularpay.otp.models.CreateOtpResponse;
import io.angularpay.otp.models.ValidateOtpAApiModel;
import io.angularpay.otp.models.ValidateOtpResponse;

import java.util.Map;

public interface RestApiPort {

    CreateOtpResponse createOtp(CreateOtpAApiModel createOtpAApiModel, Map<String, String> headers);
    ValidateOtpResponse validateOtp(ValidateOtpAApiModel validateOtpAApiModel, Map<String, String> headers);
}
