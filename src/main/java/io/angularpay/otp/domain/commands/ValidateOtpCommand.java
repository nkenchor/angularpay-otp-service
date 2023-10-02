package io.angularpay.otp.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.otp.adapters.outbound.MongoAdapter;
import io.angularpay.otp.configurations.AngularPayConfiguration;
import io.angularpay.otp.domain.OtpRequest;
import io.angularpay.otp.exceptions.CommandException;
import io.angularpay.otp.exceptions.ErrorObject;
import io.angularpay.otp.models.ValidateOtpCommandRequest;
import io.angularpay.otp.models.ValidateOtpResponse;
import io.angularpay.otp.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.angularpay.otp.common.Constants.ERROR_SOURCE;
import static io.angularpay.otp.exceptions.ErrorCode.*;
import static io.angularpay.otp.helpers.CommandHelper.getRequestByDeviceReferenceOrThrow;

@Slf4j
@Service
public class ValidateOtpCommand extends AbstractCommand<ValidateOtpCommandRequest, ValidateOtpResponse> {

    private final DefaultConstraintValidator validator;
    private final MongoAdapter mongoAdapter;
    private final AngularPayConfiguration configuration;

    public ValidateOtpCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            MongoAdapter mongoAdapter,
            AngularPayConfiguration configuration) {
        super("ValidateOtpCommand", mapper);
        this.validator = validator;
        this.mongoAdapter = mongoAdapter;
        this.configuration = configuration;
    }

    @Override
    protected String getResourceOwner(ValidateOtpCommandRequest request) {
        return request.getAuthenticatedUser().getUserReference();
    }

    @Override
    protected ValidateOtpResponse handle(ValidateOtpCommandRequest request) {
        OtpRequest found = getRequestByDeviceReferenceOrThrow(mongoAdapter, request.getValidateOtpAApiModel().getDeviceReference());

        if (found.isStrict()) {
            if (!StringUtils.hasText(request.getValidateOtpAApiModel().getUserReference())
            || !request.getValidateOtpAApiModel().getUserReference().equalsIgnoreCase(found.getUserReference())) {
                throw CommandException.builder()
                        .status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .errorCode(STRICT_VALIDATION_ERROR)
                        .message(STRICT_VALIDATION_ERROR.getDefaultMessage())
                        .build();
            }
        }

        boolean isValidExpiry = Instant.parse(found.getExpiresAt()).isAfter(Instant.now());
        if (!isValidExpiry) {
            throw CommandException.builder()
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .errorCode(EXPIRED_OTP_CODE_ERROR)
                    .message(EXPIRED_OTP_CODE_ERROR.getDefaultMessage())
                    .build();
        }

        boolean isValidOtpCode = found.getCode().equalsIgnoreCase(request.getValidateOtpAApiModel().getCode());
        return ValidateOtpResponse.builder()
                .valid(isValidOtpCode)
                .build();
    }

    @Override
    protected List<ErrorObject> validate(ValidateOtpCommandRequest request) {
        return this.validator.validate(request);
    }

}
