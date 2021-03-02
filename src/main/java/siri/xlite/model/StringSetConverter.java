package siri.xlite.model;

import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Converter
public class StringSetConverter implements AttributeConverter<Set<String>, String> {

    public static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        return CollectionUtils.isNotEmpty(attribute) ? String.join(DELIMITER, attribute) : null;
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        LinkedHashSet<String> result = new LinkedHashSet<>();
        if (isNotEmpty(dbData)) {
            result.addAll(Arrays.asList(dbData.split(DELIMITER)));
        }
        return result;
    }

}