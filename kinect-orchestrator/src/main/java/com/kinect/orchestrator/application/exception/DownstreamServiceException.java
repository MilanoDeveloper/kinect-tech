package com.kinect.orchestrator.application.exception;

public class DownstreamServiceException extends RuntimeException {
    private final int downstreamStatus;

    public DownstreamServiceException(int downstreamStatus, String message) {
        super(message);
        this.downstreamStatus = downstreamStatus;
    }

    public int getDownstreamStatus() {
        return downstreamStatus;
    }
}
