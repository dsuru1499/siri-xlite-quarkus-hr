package siri.xlite.service;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Slf4j
@ApplicationScoped
public class PurgeService {

    private static final String PURGE = "PURGE";
    private static final String DELETE = "DELETE";
    private static final String NAME = "NAME";

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final HttpRequest.Builder builder = HttpRequest.newBuilder();

    public boolean delete(String uri, String rule) {
        boolean result = false;

        try {
            HttpRequest request = builder.uri(URI.create(uri)).method(DELETE, HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(1)).header(NAME, rule).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            result = (response.statusCode() == 200);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public boolean purge(String uri) {
        boolean result = false;

        try {
            HttpRequest request = builder.uri(URI.create(uri)).method(PURGE, HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(1)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            result = (response.statusCode() == 200);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

}
