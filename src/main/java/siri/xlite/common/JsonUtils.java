package siri.xlite.common;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.function.Consumer;

import static siri.xlite.common.DateTimeUtils.toLocalTime;

public class JsonUtils {

    public static final String VERSION = "version";
    public static final String HREF = "href";
    public static final String ID = "_id";

    private static final JsonFactory factory = new JsonFactory();

    public static void writeStartDocument(JsonGenerator writer, siri.xlite.common.Parameters parameters) {
        if (writer.isClosed()) {
            ExceptionUtils.wrapAndThrow(new Exception());
        }
    }

    public static void writeEndDocument(JsonGenerator writer) {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static JsonGenerator createJsonWriter(OutputStream out) {
        JsonGenerator result = null;
        try {
            result = factory.createGenerator(out);
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
        return result;
    }

    // public static void writeIntegerField(JsonGenerator writer, String name, Document source) {
    // writeField(writer, name, source.getInteger(name));
    // }
    //
    // public static void writeLongField(JsonGenerator writer, String name, Document source) {
    // writeField(writer, name, source.getLong(name));
    // }
    //
    // public static void writeBooleanField(JsonGenerator writer, String name, Document source) {
    // writeField(writer, name, source.getBoolean(name));
    // }
    //
    // public static void writeDoubleField(JsonGenerator writer, String name, Document source) {
    // writeField(writer, name, source.getDouble(name));
    // }
    //
    // public static void writeLocalTimeField(JsonGenerator writer, String name, Document source) {
    // writeField(writer, name, toLocalTime(source.getDate(name)));
    // }
    //
    // public static void writeDurationField(JsonGenerator writer, String name, Document source) {
    // Long duration = source.getLong(name);
    // writeField(writer, name, SiriStructureFactory.createDuration(duration));
    // }
    //
    // public static void writeArrayField(JsonGenerator writer, String name, Document source) {
    // writeArray(writer, name, source.get(name, List.class));
    // }
    //
    // public static <T> void writeArrayField(JsonGenerator writer, String name, Document source, Consumer<T> consumer)
    // {
    // writeArray(writer, name, source.get(name, List.class), consumer);
    // }
    //
    // public static void writeObjectField(JsonGenerator writer, String name, Document source, Consumer<Document>
    // consumer) {
    // writeObject(writer, name, (Document) source.get(name), consumer);
    // }

    public static void writeField(JsonGenerator writer, String name, String value) {
        try {
            if (StringUtils.isNotEmpty(value)) {
                writer.writeStringField(name, value);
            }

        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, Date value) {
        writeField(writer, name, toLocalTime(value));
    }

    public static void writeField(JsonGenerator writer, String name, LocalDateTime value) {
        try {
            if (value != null) {
                writer.writeStringField(name, value.format(DateTimeFormatter.ISO_DATE_TIME));
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, LocalTime value) {
        try {
            if (value != null) {
                writer.writeStringField(name, value.format(DateTimeFormatter.ISO_TIME));
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, Boolean value) {
        try {
            if (value != null) {
                writer.writeBooleanField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, Integer value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, Long value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, Float value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, Double value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, BigDecimal value) {
        try {
            if (value != null) {
                writer.writeNumberField(name, value);
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static void writeField(JsonGenerator writer, String name, Enum<?> value) {
        try {
            if (value != null) {
                writer.writeStringField(name, value.name());
            }

        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static <T> void writeArray(JsonGenerator writer, String name, Collection<?> values) {
        try {
            if (CollectionUtils.isNotEmpty(values)) {
                writer.writeArrayFieldStart(name);
                for (Object value : values) {
                    writer.writeString(value.toString());
                }
                writer.writeEndArray();
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static <T> void writeArray(JsonGenerator writer, String name, Collection<T> values, Consumer<T> consumer) {
        try {
            if (CollectionUtils.isNotEmpty(values)) {
                writer.writeArrayFieldStart(name);
                values.forEach(consumer);
                writer.writeEndArray();
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static <T> void writeObject(JsonGenerator writer, T value, Consumer<T> consumer) {
        try {
            if (value != null) {
                writer.writeStartObject();
                consumer.accept(value);
                writer.writeEndObject();
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static <T> void writeObject(JsonGenerator writer, String name, T value, Consumer<T> consumer) {
        try {
            if (value != null) {
                writer.writeObjectFieldStart(name);
                consumer.accept(value);
                writer.writeEndObject();
            }
        } catch (IOException e) {
            ExceptionUtils.wrapAndThrow(e);
        }
    }

    public static <T, E extends Exception> Consumer<T> wrapper(ConsumerWithException<T, E> fn) {
        return t -> {
            try {
                fn.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static <T, E extends Exception> Runnable wrapper(RunnableWithException<E> fn) {
        return () -> {
            try {
                fn.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    @FunctionalInterface
    interface ConsumerWithException<T, E extends Throwable> {
        void accept(T t) throws E;
    }

    @FunctionalInterface
    interface RunnableWithException<E extends Throwable> {
        void run() throws E;

    }

}