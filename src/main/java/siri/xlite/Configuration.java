package siri.xlite;

import io.quarkus.arc.config.ConfigProperties;
import lombok.Getter;
import lombok.Setter;

@ConfigProperties(prefix = "siri-xlite")
@Getter
@Setter
public class Configuration {

    private ServiceConfiguration linesDiscovery;
    private ServiceConfiguration stopPointsDiscovery;
    private ServiceConfiguration stopMonitoring;
    private ServiceConfiguration estimatedTimetable;
    private ServiceConfiguration estimatedVehicleJourney;

    @Getter
    @Setter
    public static class ServiceConfiguration {
        public Integer maxAge = 0;
        public Integer sMaxage = 0;
    }
}
