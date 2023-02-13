package com.sample.cardinal.resource.view;

import com.google.inject.Inject;
import com.sample.cardinal.config.ApplicationConfig;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/login")
public class AccountLoginResource {

    private final String clientId;
    private final String redirectUri;
    private final String response;
    private final String scope;
    private final String rabbitLoginUrl;

    @Inject
    public AccountLoginResource(ApplicationConfig config) {
        this.clientId = config.clientId();
        this.redirectUri = config.redirectUri();
        this.response = config.responseType();
        this.scope = config.scope();
        this.rabbitLoginUrl = config.oauthAuthorizationUrl();
    }

    @GET
    public Response login() {


        String redirectLocation = String.format("%s?client_id=%s&redirect_uri=%s&response_type=%s&scope=%s", this.rabbitLoginUrl, this.clientId, this.redirectUri, this.response, this.scope);

        return getSuccessPath(redirectLocation);
    }

    private Response getSuccessPath(String redirectPath) {
        String content =  new StringBuilder().append("<html>\n")
                .append("<head><title>Login To Application</title>")
                .append("<script lang=\"javascript\">document.location=\"").append(redirectPath).append("\"</script>")
                .append("</head>\n")
                .append("<body>\n")
                .append("<h1>").append("Login Initiating").append("</h1>\n")
                .append("<p>")
                .append("Your Briowser Should Automatically redirect, if it doesn't please click on the <a href=\"").append(redirectPath).append("\">link</a>")
                .append("</p>\n")
                .append("</body>\n")
                .toString();
        return Response.ok().entity(content).build();
    }


    private Response getErrorPage(String title, String message) {
        String content =  new StringBuilder().append("<html>\n")
                .append("<head><title>Login To Application</title></head>\n")
                .append("<body>\n")
                .append("<h1 id=\"err_msg\">").append(title).append("</h1>\n")
                .append("<p>")
                .append(message)
                .append("</p>\n")
                .append("</body>\n")
                .toString();

        return Response.ok().entity(content).build();
    }

}
