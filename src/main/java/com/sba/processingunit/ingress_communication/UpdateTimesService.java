package com.sba.processingunit.ingress_communication;

import com.sba.processingunit.cache.CacheType;
import com.sba.processingunit.cache.HazelcastClientGenerator;
import org.springframework.stereotype.Service;

@Service
public class UpdateTimesService {
    private final String USERS_KEY = "users";

    public void update() {
        HazelcastClientGenerator.getClient()
                .getMap(CacheType.UPDATES.value())
                .replace(USERS_KEY, System.currentTimeMillis());
    }
}
