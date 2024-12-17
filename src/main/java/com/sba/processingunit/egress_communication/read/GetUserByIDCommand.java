package com.sba.processingunit.egress_communication.read;

import com.sba.processingunit.egress_communication.shared.CommandType;

public record GetUserByIDCommand(String id) implements UserFetchCommand {
    @Override
    public CommandType getType() {
        return CommandType.GET;
    }

    @Override
    public String getCommandKey() {
        return id;
    }
}
