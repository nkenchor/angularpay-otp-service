package io.angularpay.otp.domain.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.angularpay.otp.adapters.outbound.MongoAdapter;
import io.angularpay.otp.configurations.AngularPayConfiguration;
import io.angularpay.otp.domain.OtpRequest;
import io.angularpay.otp.exceptions.ErrorObject;
import io.angularpay.otp.models.CreateOtpCommandRequest;
import io.angularpay.otp.models.CreateOtpResponse;
import io.angularpay.otp.validation.DefaultConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static io.angularpay.otp.common.Constants.ERROR_SOURCE;
import static io.angularpay.otp.exceptions.ErrorCode.VALIDATION_ERROR;
import static io.angularpay.otp.helpers.CommandHelper.getRequestByReferenceOrThrow;

@Slf4j
@Service
public class CreateOtpCommand extends AbstractCommand<CreateOtpCommandRequest, CreateOtpResponse> {

    private final DefaultConstraintValidator validator;
    private final MongoAdapter mongoAdapter;
    private final AngularPayConfiguration configuration;

    public CreateOtpCommand(
            ObjectMapper mapper,
            DefaultConstraintValidator validator,
            MongoAdapter mongoAdapter,
            AngularPayConfiguration configuration) {
        super("CreateOtpCommand", mapper);
        this.validator = validator;
        this.mongoAdapter = mongoAdapter;
        this.configuration = configuration;
    }

    @Override
    protected String getResourceOwner(CreateOtpCommandRequest request) {
        return request.getAuthenticatedUser().getUserReference();
    }

    @Override
    protected CreateOtpResponse handle(CreateOtpCommandRequest request) {
        if (StringUtils.hasText(request.getCreateOtpAApiModel().getUserReference())) {
            this.mongoAdapter.deleteOtpRequestByUserReference(request.getCreateOtpAApiModel().getUserReference());
        }
        if (StringUtils.hasText(request.getCreateOtpAApiModel().getDeviceReference())) {
            this.mongoAdapter.deleteOtpRequestByDeviceReference(request.getCreateOtpAApiModel().getDeviceReference());
        }

        String code = IntStream.range(0, 6).parallel()
                .mapToObj(x -> String.valueOf(new Random().nextInt(10)))
                .collect(Collectors.joining());

        OtpRequest otpRequest = OtpRequest.builder()
                .reference(UUID.randomUUID().toString())
                .strict(request.getCreateOtpAApiModel().getStrict())
                .deviceReference(request.getCreateOtpAApiModel().getDeviceReference())
                .userReference(request.getCreateOtpAApiModel().getUserReference())
                .expiresAt(Instant.now().plusSeconds(this.configuration.getDefaultOtpExpiryInSeconds())
                        .truncatedTo(ChronoUnit.SECONDS).toString())
                .code(code)
                .build();

        OtpRequest response = this.mongoAdapter.createOtpRequest(otpRequest);

        Executors.newScheduledThreadPool(1).schedule(
                () -> {
                    OtpRequest found = getRequestByReferenceOrThrow(this.mongoAdapter, response.getReference());
                    mongoAdapter.deleteOtpRequest(found);
                },
                configuration.getDefaultOtpExpiryInSeconds(),
                TimeUnit.SECONDS
        );

        return CreateOtpResponse.builder()
                .reference(response.getReference())
                .code(response.getCode())
                .build();
    }

    @Override
    protected List<ErrorObject> validate(CreateOtpCommandRequest request) {
        List<ErrorObject> errors = new ArrayList<>();
        if (Objects.nonNull(request.getCreateOtpAApiModel().getStrict())
                && request.getCreateOtpAApiModel().getStrict()) {
            if (!StringUtils.hasText(request.getCreateOtpAApiModel().getUserReference())) {
                errors.add(ErrorObject.builder()
                        .code(VALIDATION_ERROR)
                        .message("user_reference must not be empty for strict OTP creation")
                        .source(ERROR_SOURCE)
                        .build());
            }
        }
        errors.addAll(this.validator.validate(request));
        return errors;
    }

}
