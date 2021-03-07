package siri.xlite.common;

import com.fasterxml.jackson.core.JsonGenerator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.exceptions.Exceptions;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import siri.xlite.common.marshaller.json.SiriExceptionMarshaller;
import siri.xlite.model.SiriEntity;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static siri.xlite.common.JsonUtils.createJsonWriter;
import static siri.xlite.common.SiriService.APPLICATION;

@Slf4j
public abstract class SiriSubscriber<T extends SiriEntity, P extends siri.xlite.common.DefaultParameters> implements Subscriber<T> {

    private static final String CONTENT_TYPE_VALUE = APPLICATION_JSON + "; charset=utf-8";

    @Inject
    protected P parameters;

    protected RoutingContext context;
    protected ByteArrayOutputStream out;
    protected JsonGenerator writer;

    public static String baseURI(HttpServerRequest request) {
        URL url = null;
        try {
            url = new URL(request.absoluteURI());
        } catch (MalformedURLException e) {
            ExceptionUtils.wrapAndThrow(e);
        }

        return url.getProtocol() + "://" + url.getAuthority() + APPLICATION;
    }

    public void configure(P parameters, RoutingContext context) {
        this.parameters = parameters;
        this.context = context;
        this.out = new ByteArrayOutputStream(1000 * 1024);
        this.writer = createJsonWriter(this.out);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onError(Throwable t) {
        try {
            if (t instanceof siri.xlite.common.NotModifiedException) {
                writeNotModified();
            } else if (t instanceof siri.xlite.common.SiriException) {
                writeSiriException((siri.xlite.common.SiriException) t);
            } else if (t != null) {
                writeSiriException(siri.xlite.common.SiriException.createOtherError(t));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Exceptions.propagate(e);
        }
    }

    protected void writeSiriException(siri.xlite.common.SiriException e) throws Exception {
        log.error(e.getMessage(), e);
        SiriExceptionMarshaller.getInstance().write(writer, e);
        writer.close();
        HttpServerResponse response = this.context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE)
                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
        // log(response);
        response.end(out.toString());
    }

    protected void writeNotFound() throws Exception {
        SiriExceptionMarshaller.getInstance().write(writer, siri.xlite.common.SiriException.createInvalidDataReferencesError());
        writer.close();
        HttpServerResponse response = this.context.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code());
        // log(response);
        response.end(out.toString());
    }

    protected void writeNotModified() throws Exception {
        writer.close();
        HttpServerResponse response = this.context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE)
                .putHeader(HttpHeaders.CACHE_CONTROL,
                        Arrays.asList(siri.xlite.common.CacheControl.MUST_REVALIDATE, siri.xlite.common.CacheControl.PROXY_REVALIDATE,
                                siri.xlite.common.CacheControl.S_MAX_AGE + parameters.getSMaxAge(),
                                siri.xlite.common.CacheControl.MAX_AGE + parameters.getMaxAge()))
                .putHeader(HttpHeaders.LAST_MODIFIED, DateTimeUtils.toRFC1123(siri.xlite.common.CacheControl.getLastModified(context)))
                .setStatusCode(HttpURLConnection.HTTP_NOT_MODIFIED);
//        log(response);
        response.end();
    }

    protected void writeResponse(Date lastModified) {
        HttpServerResponse response = this.context.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE_VALUE)
                .putHeader(HttpHeaders.CACHE_CONTROL,
                        Arrays.asList(siri.xlite.common.CacheControl.MUST_REVALIDATE, siri.xlite.common.CacheControl.PROXY_REVALIDATE,
                                siri.xlite.common.CacheControl.S_MAX_AGE + parameters.getSMaxAge(),
                                siri.xlite.common.CacheControl.MAX_AGE + parameters.getMaxAge()))
                .putHeader(HttpHeaders.LAST_MODIFIED, DateTimeUtils.toRFC1123(lastModified));
        // log(response);
        response.end(out.toString());
    }

    private void log(HttpServerResponse response) {
        log.info(siri.xlite.common.Color.GREEN + String.format("[DSU] %d : %s", response.getStatusCode(), response.getStatusMessage())
                + siri.xlite.common.Color.NORMAL);
        MultiMap headers = response.headers();
        for (String key : headers.names()) {
            String value = String.join(",", headers.getAll(key));
            log.info(siri.xlite.common.Color.GREEN + key + "=" + value + siri.xlite.common.Color.NORMAL);
        }

    }
}