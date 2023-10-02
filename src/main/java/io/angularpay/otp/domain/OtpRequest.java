
package io.angularpay.otp.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Document("otp_requests")
public class OtpRequest {

    @Id
    private String id;
    @Version
    private int version;
    private String reference;
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("expires_at")
    private String expiresAt;
    private String code;
    private boolean strict;
    @JsonProperty("device_reference")
    private String deviceReference;
    @JsonProperty("user_reference")
    private String userReference;

}
