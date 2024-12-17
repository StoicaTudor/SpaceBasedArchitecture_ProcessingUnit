package com.sba.processingunit.ingress_communication.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sba.processingunit.ingress_communication.handlers.DataReaderUserHandler;
import com.sba.processingunit.ingress_communication.responses.AllUsersResponse;
import com.sba.processingunit.ingress_communication.responses.PartialMetaDataResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DataReaderUserListener {

    private final DataReaderUserHandler dataReaderUserHandler;

    public DataReaderUserListener(DataReaderUserHandler dataReaderUserHandler) {
        this.dataReaderUserHandler = dataReaderUserHandler;
    }

//    @KafkaListener(topics = "DataReaderReadTopic")
//    void userDataListener(String payload) throws JsonProcessingException, JsonMappingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<User> users = objectMapper.readValue(
//                payload,
//                objectMapper.getTypeFactory().constructCollectionType(
//                        List.class,
//                        User.class
//                )
//        );
//        System.out.println(users);
//    }

    @KafkaListener(topics = "DataReaderReadTopic")
    void userDataListener(String payload) throws JsonProcessingException, JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        final PartialMetaDataResponse partialMetaDataResponse = objectMapper.readValue(payload, PartialMetaDataResponse.class);

        switch (partialMetaDataResponse.responseType()) {
            case GET_ALL_USERS -> dataReaderUserHandler.replaceAllUsersInCache(
                    objectMapper.readValue(payload, AllUsersResponse.class).users()
            );
            case GET_USER -> System.out.println("GET_USER");
        }
        ;
    }
}
