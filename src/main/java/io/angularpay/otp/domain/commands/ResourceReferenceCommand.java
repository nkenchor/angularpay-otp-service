package io.angularpay.otp.domain.commands;

public interface ResourceReferenceCommand<T, R> {

    R map(T referenceResponse);
}
