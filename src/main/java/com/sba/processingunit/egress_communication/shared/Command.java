package com.sba.processingunit.egress_communication.shared;

import java.io.Serializable;

public interface Command extends Serializable {
    CommandType getType();
}
