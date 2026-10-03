package com.kinect.orchestrator.adapters.inbound.controller;

import com.kinect.orchestrator.application.exception.DownstreamServiceException;
import com.kinect.orchestrator.application.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

@RestControllerAdvice
public class OrchestrationExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(OrchestrationExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(DownstreamServiceException.class)
    public ResponseEntity<Map<String, String>> handleDownstreamStatus(DownstreamServiceException exception) {
        int status = exception.getDownstreamStatus();
        if (status >= 500) {
            log.error("A downstream service returned HTTP {}", status);
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("message", "A downstream service failed"));
        }
        return ResponseEntity.status(status)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, String>> handleUnavailable(ResourceAccessException exception) {
        log.error("A downstream service could not be reached", exception);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(Map.of("message", "A downstream service is unavailable"));
    }
}
