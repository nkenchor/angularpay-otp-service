package io.angularpay.otp.helpers;

import io.angularpay.otp.adapters.outbound.MongoAdapter;
import io.angularpay.otp.domain.OtpRequest;
import io.angularpay.otp.exceptions.CommandException;
import io.angularpay.otp.exceptions.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static io.angularpay.otp.exceptions.ErrorCode.DUPLICATE_ENTRY_ERROR;
import static io.angularpay.otp.exceptions.ErrorCode.REQUEST_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommandHelper {

    public static OtpRequest getRequestByReferenceOrThrow(MongoAdapter mongoAdapter, String reference) {
        return mongoAdapter.findOtpRequestByReference(reference).orElseThrow(
                () -> commandException(REQUEST_NOT_FOUND)
        );
    }

    public static OtpRequest getRequestByDeviceReferenceOrThrow(MongoAdapter mongoAdapter, String deviceReference) {
        return mongoAdapter.findOtpRequestByDeviceReference(deviceReference).orElseThrow(
                () -> commandException(REQUEST_NOT_FOUND)
        );
    }

    public static OtpRequest getRequestByUserReferenceOrThrow(MongoAdapter mongoAdapter, String userReference) {
        return mongoAdapter.findOtpRequestByUserReference(userReference).orElseThrow(
                () -> commandException(REQUEST_NOT_FOUND)
        );
    }

    private static CommandException commandException(ErrorCode errorCode) {
        return CommandException.builder()
                .status(HttpStatus.NOT_FOUND)
                .errorCode(errorCode)
                .message(errorCode.getDefaultMessage())
                .build();
    }

}
