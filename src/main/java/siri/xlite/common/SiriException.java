package siri.xlite.common;

import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SiriException extends Exception {

    private static final long serialVersionUID = 1L;

    @Getter
    private final ERROR_CODE code;

    @Getter
    private Map<String, Object> values;

    public SiriException() {
        this(ERROR_CODE.OtherError);
    }

    public SiriException(ERROR_CODE code) {
        this(code, code.name(), null);
    }

    public SiriException(ERROR_CODE code, String description) {
        super(description);
        this.code = code;
    }

    public SiriException(ERROR_CODE code, String description, Map<String, Object> values) {
        super(description);
        this.code = code;
        this.values = values;
    }

    public SiriException(Throwable cause) {
        this(ERROR_CODE.OtherError, cause);
    }

    public SiriException(ERROR_CODE code, Throwable cause) {
        this(code, ExceptionUtils.getMessage(cause), null, cause);
    }

    public SiriException(ERROR_CODE code, String description, Map<String, Object> values, Throwable cause) {
        super(description, cause);
        this.code = code;
        this.values = values;
    }

    public static SiriException createUnapprovedKeyAccessError(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("Key", value);
        return new SiriException(ERROR_CODE.UnapprovedKeyAccessError, "Unapproved Key Access Error", map);
    }

    public static SiriException createUnknownParticipantError(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("ParticipantRef", value);
        return new SiriException(ERROR_CODE.UnknownParticipantError, "Unknown Participant Error", map);
    }

    public static SiriException createUnknownEndpointError(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("Endpoint", value);
        return new SiriException(ERROR_CODE.UnknownEndpointError, "Unknown Endpoint Error", map);
    }

    public static SiriException createEndpointDeniedAccessError(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("Endpoint", value);
        return new SiriException(ERROR_CODE.EndpointDeniedAccessError, "Endpoint Denied Access Error", map);
    }

    public static SiriException createEndpointNotAvailableAccessError(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("Endpoint", value);
        return new SiriException(ERROR_CODE.EndpointNotAvailableAccessError, "Endpoint Not Available Access Error",
                map);
    }

    public static SiriException createServiceNotAvailableError() {
        return new SiriException(ERROR_CODE.ServiceNotAvailableError, "Service Not Available Error");
    }

    public static SiriException createCapabilityNotSupportedError(String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("CapabilityRef", value);
        return new SiriException(ERROR_CODE.CapabilityNotSupportedError, "Capability Not Supported Error", map);
    }

    public static SiriException createAccessNotAllowedError() {
        return new SiriException(ERROR_CODE.AccessNotAllowedError, "Access Not Allowed Error");
    }

    public static SiriException createInvalidDataReferencesError(String... values) {
        Map<String, Object> map = new HashMap<>();
        map.put("InvalidRef", Arrays.asList(values));
        return new SiriException(ERROR_CODE.InvalidDataReferencesError, "Invalid Data References Error", map);
    }

    public static SiriException createBeyondDataHorizon() {
        return new SiriException(ERROR_CODE.BeyondDataHorizon, "Beyond Data Horizon");
    }

    public static SiriException createNoInfoForTopicError() {
        return new SiriException(ERROR_CODE.NoInfoForTopicError, "No Info For Topic Error");
    }

    public static SiriException createParametersIgnoredError(String... values) {
        Map<String, Object> map = new HashMap<>();
        map.put("ParameterName", Arrays.asList(values));
        return new SiriException(ERROR_CODE.ParametersIgnoredError, "Parameters Ignored Error", map);
    }

    public static SiriException createUnknownExtensionsError(String... values) {
        Map<String, Object> map = new HashMap<>();
        map.put("ExtensionName", Arrays.asList(values));
        return new SiriException(ERROR_CODE.UnknownExtensionsError, "Unknown Extensions Error", map);
    }

    public static SiriException createAllowedResourceUsageExceededError() {
        return new SiriException(ERROR_CODE.AllowedResourceUsageExceededError, "Allowed Resource Usage Exceeded Error");
    }

    public static SiriException createOtherError(Throwable e) {
        return new SiriException(ERROR_CODE.OtherError, e);
    }

    public enum ERROR_CODE {
        UnapprovedKeyAccessError, UnknownParticipantError, UnknownEndpointError, EndpointDeniedAccessError, EndpointNotAvailableAccessError, ServiceNotAvailableError, CapabilityNotSupportedError, AccessNotAllowedError, InvalidDataReferencesError, BeyondDataHorizon, NoInfoForTopicError, ParametersIgnoredError, UnknownExtensionsError, AllowedResourceUsageExceededError, OtherError
    }

}
