package com.sba.processingunit.cache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;

public class HazelcastClientGenerator {
    private static HazelcastInstance client;

    public static HazelcastInstance getClient() {
        if (client == null) client = newHazelcastClient();
        return client;
    }

    private static HazelcastInstance newHazelcastClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.getNetworkConfig()
//                .addAddress("127.0.0.1")
                .setRedoOperation(true);

        // Create and return the Hazelcast client instance
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
