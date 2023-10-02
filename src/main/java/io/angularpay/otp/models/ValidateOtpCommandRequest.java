package io.angularpay.otp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ValidateOtpCommandRequest extends AccessControl {

    @NotNull
    @Valid
    private ValidateOtpAApiModel validateOtpAApiModel;

    ValidateOtpCommandRequest(AuthenticatedUser authenticatedUser) {
        super(authenticatedUser);
    }
}
