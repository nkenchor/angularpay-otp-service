package io.angularpay.otp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateOtpAApiModel {

    @NotNull
    private Boolean strict;

    @NotEmpty
    @JsonProperty("device_reference")
    private String deviceReference;

    @JsonProperty("user_reference")
    private String userReference;
}
