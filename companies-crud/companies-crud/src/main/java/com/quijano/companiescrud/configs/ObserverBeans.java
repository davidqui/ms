package com.quijano.companiescrud.configs;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.tracing.annotation.MethodInvocationProcessor;
import io.micrometer.tracing.annotation.SpanAspect;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.logs.OtlpHttpLogRecordExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.logs.LogRecordProcessor;
import io.opentelemetry.sdk.logs.SdkLoggerProvider;
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
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

    @Bean
    public OpenTelemetry openTelemetry(SdkLoggerProvider loggerProvider,
                                       SdkTracerProvider tracerProvider,
                                       ContextPropagators contextPropagators){
        return OpenTelemetrySdk
                .builder()
                    .setLoggerProvider(loggerProvider)
                    .setTracerProvider(tracerProvider)
                    .setPropagators(contextPropagators)
                .build();
    }

    @Bean
    public LogRecordProcessor logRecordProcessor(){
        var otlpLogRecord = OtlpHttpLogRecordExporter
                .builder()
                .setEndpoint("http://localhost:4317")
                .build();

        return BatchLogRecordProcessor
                .builder(otlpLogRecord).build();
    }

    public SpanAspect spanAspect(MethodInvocationProcessor processor){
        return new SpanAspect(processor);
    }
}
