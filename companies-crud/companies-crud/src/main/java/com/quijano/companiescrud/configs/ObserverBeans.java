package com.quijano.companiescrud.configs;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ObserverBeans {

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry){
        return new ObservedAspect(observationRegistry);

    }

    @Bean
    public SdkLoggerProvider sdkLoggerProvider(Environment env, ObjectProvider<LogRecordProcessor> processor){

        var applicationName = env.getProperty("spring.application.name", "application");

        var springResource = Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, applicationName));
        var builder  = SdkLoggerProvider.builder()
                .setResource(Resource.getDefault().merge(springResource));
        processor.orderedStream().forEach(builder::addLogRecordProcessor);
        return builder.build();
    }
}
