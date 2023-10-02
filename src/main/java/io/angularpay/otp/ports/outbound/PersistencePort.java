package io.angularpay.otp.ports.outbound;

import io.angularpay.otp.domain.OtpRequest;

import java.util.Optional;

public interface PersistencePort {
    OtpRequest createOtpRequest(OtpRequest request);
    OtpRequest updateOtpRequest(OtpRequest request);
    Optional<OtpRequest> findOtpRequestByReference(String reference);
    Optional<OtpRequest> findOtpRequestByDeviceReference(String deviceReference);
    Optional<OtpRequest> findOtpRequestByUserReference(String userReference);
    void deleteOtpRequestByDeviceReference(String deviceReference);
    void deleteOtpRequestByUserReference(String userReference);
    void deleteOtpRequest(OtpRequest request);
}
