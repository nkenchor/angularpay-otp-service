package io.angularpay.otp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ValidateOtpAApiModel {

    @NotEmpty
    private String code;

    @NotEmpty
    @JsonProperty("device_reference")
    private String deviceReference;

    @JsonProperty("user_reference")
    private String userReference;
}
