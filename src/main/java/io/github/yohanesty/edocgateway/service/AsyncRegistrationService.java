package io.github.yohanesty.edocgateway.service;

import io.github.yohanesty.edocgateway.model.DummyMessage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncRegistrationService {
    private final ExecutorService executor = Executors.newFixedThreadPool(2);

    public void register(final DummyMessage message) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                // Portfolio-only downstream simulation.
                System.out.println("[ASYNC-REGISTER] requestId=" + message.getRequestId());
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
