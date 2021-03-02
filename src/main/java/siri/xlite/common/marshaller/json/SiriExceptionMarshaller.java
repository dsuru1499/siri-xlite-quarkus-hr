package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import siri.xlite.common.SiriException;

import java.util.Collection;
import java.util.Map;

import static siri.xlite.common.JsonUtils.writeField;
import static siri.xlite.common.JsonUtils.writeObject;

public class SiriExceptionMarshaller implements Marshaller<SiriException> {

    public static final String ERROR_TEXT = "errorText";
    public static final String ERROR_CODE = "errorCode";

    @Getter
    private static final Marshaller<SiriException> instance = new SiriExceptionMarshaller();

    @Override
    public void write(JsonGenerator writer, SiriException e) {

        switch (e.getCode()) {
            case UnapprovedKeyAccessError:
            case UnknownParticipantError:
            case UnknownEndpointError:
            case EndpointDeniedAccessError:
            case EndpointNotAvailableAccessError:
            case ServiceNotAvailableError:
            case CapabilityNotSupportedError:
            case AccessNotAllowedError:
            case InvalidDataReferencesError:
            case BeyondDataHorizon:
            case NoInfoForTopicError:
            case ParametersIgnoredError:
            case UnknownExtensionsError:
            case AllowedResourceUsageExceededError:
            case OtherError: {

                writeObject(writer, e, t -> {
                    writeField(writer, ERROR_CODE, e.getCode().name());
                    writeField(writer, ERROR_TEXT, ExceptionUtils.getMessage(t));
                    Map<String, Object> values = t.getValues();
                    if (values != null) {
                        for (Map.Entry<String, Object> entry : values.entrySet()) {
                            if (entry.getValue() instanceof String) {
                                String value = (String) entry.getValue();
                                writeField(writer, entry.getKey(), value);
                            } else if (entry.getValue() instanceof Collection) {
                                Collection<?> list = (Collection<?>) entry.getValue();
                                for (Object o : list) {
                                    if (o instanceof String) {
                                        String value = (String) o;
                                        writeField(writer, entry.getKey(), value);
                                    }
                                }
                            }
                        }
                    }
                });
                break;
            }
            default:
                break;
        }
    }

}
