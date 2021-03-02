package siri.xlite.common;

import io.vertx.ext.web.RoutingContext;
import siri.xlite.Configuration;

public interface Parameters {

    void configure(Configuration configuration, RoutingContext context) throws SiriException;

    void validate() throws SiriException;

}