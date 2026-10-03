package com.kinect.orchestrator.adapters.inbound.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/{service:persons|payments|training-programs}/**")
public class ServiceGatewayController {
    private static final Logger log = LoggerFactory.getLogger(ServiceGatewayController.class);

    private final RestClient restClient;
    private final Map<String, String> serviceUrls;

    public ServiceGatewayController(
            @Value("${kinect.services.persons-url}") String personsUrl,
            @Value("${kinect.services.payments-url}") String paymentsUrl,
            @Value("${kinect.services.training-programs-url}") String trainingProgramsUrl) {
        this.restClient = RestClient.create();
        this.serviceUrls = Map.of(
                "persons", personsUrl,
                "payments", paymentsUrl,
                "training-programs", trainingProgramsUrl);
    }

    @RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.GET)
    public ResponseEntity<String> get(@PathVariable("service") String service, HttpServletRequest request) {
        return forward(HttpMethod.GET, service, request, null);
    }

    @RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.POST)
    public ResponseEntity<String> post(
            @PathVariable("service") String service, HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return forward(HttpMethod.POST, service, request, body);
    }

    @RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.PUT)
    public ResponseEntity<String> put(
            @PathVariable("service") String service, HttpServletRequest request,
            @RequestBody(required = false) String body) {
        return forward(HttpMethod.PUT, service, request, body);
    }

    @RequestMapping(method = org.springframework.web.bind.annotation.RequestMethod.DELETE)
    public ResponseEntity<String> delete(@PathVariable("service") String service, HttpServletRequest request) {
        return forward(HttpMethod.DELETE, service, request, null);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, String>> handleServiceUnavailable(ResourceAccessException exception) {
        log.warn("A downstream service could not be reached", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("message", "A downstream service is unavailable"));
    }

    private ResponseEntity<String> forward(
            HttpMethod method, String service, HttpServletRequest request, String body) {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        String target = serviceUrls.get(service).replaceAll("/+$", "") + path;
        if (request.getQueryString() != null) {
            target += "?" + request.getQueryString();
        }

        RestClient.RequestBodySpec downstreamRequest = restClient.method(method).uri(target);
        if (body != null) {
            downstreamRequest.contentType(MediaType.APPLICATION_JSON).body(body);
        }

        return downstreamRequest.exchange((clientRequest, clientResponse) -> {
            byte[] responseBody = clientResponse.getBody().readAllBytes();
            if (responseBody.length == 0) {
                return ResponseEntity.status(clientResponse.getStatusCode()).build();
            }
            String responseText = new String(responseBody, StandardCharsets.UTF_8);
            return ResponseEntity.status(clientResponse.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(responseText);
        });
    }
}
