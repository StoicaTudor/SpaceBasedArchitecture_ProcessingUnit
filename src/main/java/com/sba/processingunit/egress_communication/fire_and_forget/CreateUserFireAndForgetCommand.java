package com.sba.processingunit.egress_communication.fire_and_forget;

import com.sba.processingunit.egress_communication.shared.CommandType;
import com.sba.processingunit.data.User;

public record CreateUserFireAndForgetCommand(User user) implements UserFireAndForgetCommand {
    @Override
    public CommandType getType() {
        return CommandType.CREATE;
    }

    @Override
    public User getUser() {
        return user();
    }
}
