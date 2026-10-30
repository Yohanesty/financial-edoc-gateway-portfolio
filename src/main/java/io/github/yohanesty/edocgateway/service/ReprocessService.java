package io.github.yohanesty.edocgateway.service;

import io.github.yohanesty.edocgateway.model.DummyMessage;
import io.github.yohanesty.edocgateway.model.ErrorRecord;
import io.github.yohanesty.edocgateway.protocol.DummyMessageCodec;
import io.github.yohanesty.edocgateway.repository.ErrorRecordRepository;

public class ReprocessService {
    private final ErrorRecordRepository repository;
    private final DummyMessageCodec codec;
    private final MessageValidator validator;
    private final AsyncRegistrationService registrationService;

    public ReprocessService(ErrorRecordRepository repository,
                            DummyMessageCodec codec,
                            MessageValidator validator,
                            AsyncRegistrationService registrationService) {
        this.repository = repository;
        this.codec = codec;
        this.validator = validator;
        this.registrationService = registrationService;
    }

    public boolean reprocess(String errorId) {
        ErrorRecord record = repository.find(errorId);
        if (record == null) {
            return false;
        }

        try {
            DummyMessage message = codec.parse(record.getRawMessage());
            validator.validate(message);
            registrationService.register(message);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
