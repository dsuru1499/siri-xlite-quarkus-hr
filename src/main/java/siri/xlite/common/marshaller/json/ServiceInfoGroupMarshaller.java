package siri.xlite.common.marshaller.json;

import com.fasterxml.jackson.core.JsonGenerator;
import lombok.Getter;
import siri.xlite.model.VehicleJourney;

import static siri.xlite.common.JsonUtils.writeArray;
import static siri.xlite.common.JsonUtils.writeField;

public class ServiceInfoGroupMarshaller implements Marshaller<VehicleJourney> {

    public static final String OPERATOR_REF = "operatorRef";
    public static final String PRODUCT_CATEGORY_REF = "productCategoryRef";
    public static final String SERVICE_FEATURE_REFS = "serviceFeatureRefs";
    public static final String VEHICLE_FEATURE_REFS = "vehicleFeatureRefs";

    @Getter
    private static final Marshaller<VehicleJourney> instance = new ServiceInfoGroupMarshaller();

    @Override
    public void write(JsonGenerator writer, VehicleJourney source) {

        // set operatorRef
        writeField(writer, OPERATOR_REF, source.operatorRef());

        // set productCategoryRef
        writeField(writer, PRODUCT_CATEGORY_REF, source.productCategoryRef());

        // set serviceFeatureRef
        writeArray(writer, SERVICE_FEATURE_REFS, source.serviceFeatureRefs());

        // set vehicleFeatureRef
        writeArray(writer, VEHICLE_FEATURE_REFS, source.vehicleFeatureRefs());

    }
}