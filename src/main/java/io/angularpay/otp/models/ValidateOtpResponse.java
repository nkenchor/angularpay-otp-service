package io.angularpay.otp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateOtpResponse {

    @JsonProperty("is_valid")
    private boolean valid;
}
