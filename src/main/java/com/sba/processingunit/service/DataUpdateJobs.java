package com.sba.processingunit.service;

import com.sba.processingunit.egress_communication.read.GetAllUserCommand;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataUpdateJobs {
    private final long SECONDS_UPDATE_RATE = 10;

    private final DataReaderSupplier dataReaderSupplier;

    public DataUpdateJobs(DataReaderSupplier dataReaderSupplier) {
        this.dataReaderSupplier = dataReaderSupplier;
    }

    @Scheduled(fixedRate = SECONDS_UPDATE_RATE * 1000)
    public void executeJob() {
        dataReaderSupplier.sendCommand(new GetAllUserCommand());
    }
}
