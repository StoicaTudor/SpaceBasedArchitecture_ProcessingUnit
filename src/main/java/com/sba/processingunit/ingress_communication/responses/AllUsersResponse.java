package com.sba.processingunit.ingress_communication.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sba.processingunit.data.User;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AllUsersResponse(List<User> users) {
}
