package io.angularpay.otp.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOtpResponse {
    private String reference;
    private String code;
}
