package com.sba.processingunit.egress_communication.read;

import com.sba.processingunit.egress_communication.shared.Command;

public interface UserFetchCommand extends Command {
    String getCommandKey();
}
