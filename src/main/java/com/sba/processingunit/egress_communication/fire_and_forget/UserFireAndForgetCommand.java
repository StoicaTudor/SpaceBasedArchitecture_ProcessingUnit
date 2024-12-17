package com.sba.processingunit.egress_communication.fire_and_forget;

import com.sba.processingunit.egress_communication.shared.Command;
import com.sba.processingunit.data.User;

public interface UserFireAndForgetCommand extends Command {
    User getUser();
}
