package com.sba.processingunit.ingress_communication.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sba.processingunit.ingress_communication.ResponseType;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PartialMetaDataResponse(ResponseType responseType){}