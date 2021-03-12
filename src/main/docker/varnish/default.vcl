#
# This is an example VCL file for Varnish.
#
# It does not do anything by default, delegating control to the
# builtin VCL. The builtin VCL is called when there is no explicit
# return statement.
#
# See the VCL chapters in the Users Guide for a comprehensive documentation
# at https://www.varnish-cache.org/docs/.

# Marker to tell the VCL compiler that this VCL has been written with the
# 4.0 or 4.1 syntax.
vcl 4.1;
import directors;    # load the directors

acl purge {
    "172.19.0.0"/24;
	"127.0.0.1";
}

# Default backend definition. Set this to point to your content server.
backend server_1 {
    .host = "siri-xlite";
    .port = "8080";
    .probe = {
        .url = "/siri-xlite-cli/index.html";
        .interval = 10s;
        .timeout = 1s;
        .window = 5;
        .threshold = 3;
    }
}

sub vcl_init {
    new cluster = directors.round_robin();
    cluster.add_backend(server_1);
}

sub vcl_recv {
    # Happens before we check if we have this in cache already.
    #
    # Typically you clean up the request here, removing cookies you don't need,
    # rewriting the request, etc.

	if (req.method == "PURGE") {
		# Same ACL check as above:
		if (!client.ip ~ purge) {
			return(synth(405, "Not allowed."));
		}
		# curl -X PURGE http://localhost -H "regex: ^/siri-xlite/estimated-vehicle-journey/(107736416-1_191364|104339966-1_297491)"
		ban("req.url ~ " + req.http.regex);

		# Throw a synthetic page so the request won't go to the backend.
		return(synth(200, "Ban added"));
	}
}

sub vcl_backend_response {
    # Happens after we have read the response headers from the backend.
    #
    # Here you clean the response headers, removing silly Set-Cookie headers
    # and other mistakes your backend does.
}

sub vcl_deliver {
    # Happens when we have all the pieces we need, and are about to send the
    # response to the client.
    #
    # You can do accounting or modifying the final object here.
}
