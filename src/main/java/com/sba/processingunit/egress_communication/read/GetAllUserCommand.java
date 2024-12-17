package com.sba.processingunit.egress_communication.read;

import com.sba.processingunit.egress_communication.shared.CommandType;

public record GetAllUserCommand() implements UserFetchCommand {
    @Override
    public CommandType getType() {
        return CommandType.GET_ALL;
    }

    @Override
    public String getCommandKey() {
        return "GetAllUserCommand";
    }
}
