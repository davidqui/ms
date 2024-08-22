package com.quijano.companiescrud.configs;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(999)
@Slf4j
public class LogObservationHandler implements ObservationHandler<Observation.Context> {
    /**
     * Reacts to starting of an {@link Observation}.
     *
     * @param context an {@link Observation.Context}
     */
    @Override
    public void onStart(Observation.Context context) {
        log.info("LogObservationHandler::onStart: {}", context.getName());
    }

    /**
     * Reacts to an error during an {@link Observation}.
     *
     * @param context an {@link Observation.Context}
     */
    @Override
    public void onError(Observation.Context context) {
        log.info("LogObservationHandler::onError: {}", context.getName());
    }

    /**
     * Reacts to stopping of an {@link Observation}.
     *
     * @param context an {@link Observation.Context}
     */
    @Override
    public void onStop(Observation.Context context) {
        log.info("LogObservationHandler::onStop: {}", context.getName());
    }

    /**
     * Tells the registry whether this handler should be applied for a given
     * {@link Observation.Context}.
     *
     * @param context an {@link Observation.Context}
     * @return {@code true} when this handler should be used
     */
    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }
}
