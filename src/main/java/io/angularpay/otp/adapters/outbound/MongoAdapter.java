package io.angularpay.otp.adapters.outbound;

import io.angularpay.otp.domain.OtpRequest;
import io.angularpay.otp.ports.outbound.PersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MongoAdapter implements PersistencePort {

    private final OtpRequestRepository otpRequestRepository;

    @Override
    public OtpRequest createOtpRequest(OtpRequest request) {
        request.setCreatedOn(Instant.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return otpRequestRepository.save(request);
    }

    @Override
    public OtpRequest updateOtpRequest(OtpRequest request) {
        return otpRequestRepository.save(request);
    }

    @Override
    public Optional<OtpRequest> findOtpRequestByReference(String reference) {
        return otpRequestRepository.findByReference(reference);
    }

    @Override
    public Optional<OtpRequest> findOtpRequestByDeviceReference(String deviceReference) {
        return otpRequestRepository.findByDeviceReference(deviceReference);
    }

    @Override
    public Optional<OtpRequest> findOtpRequestByUserReference(String userReference) {
        return otpRequestRepository.findByUserReference(userReference);
    }

    @Override
    public void deleteOtpRequestByDeviceReference(String deviceReference) {
        otpRequestRepository.deleteByDeviceReference(deviceReference);
    }

    @Override
    public void deleteOtpRequestByUserReference(String userReference) {
        otpRequestRepository.deleteByUserReference(userReference);
    }

    @Override
    public void deleteOtpRequest(OtpRequest request) {
        otpRequestRepository.delete(request);
    }

}
