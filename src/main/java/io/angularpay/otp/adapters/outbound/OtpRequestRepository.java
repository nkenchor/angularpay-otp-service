package io.angularpay.otp.adapters.outbound;

import io.angularpay.otp.domain.OtpRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRequestRepository extends MongoRepository<OtpRequest, String> {

    Optional<OtpRequest> findByReference(String reference);
    Optional<OtpRequest> findByDeviceReference(String deviceReference);
    Optional<OtpRequest> findByUserReference(String userReference);
    void deleteByDeviceReference(String deviceReference);
    void deleteByUserReference(String userReference);
}
