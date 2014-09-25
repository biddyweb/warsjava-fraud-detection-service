package pl.warsjawa.fraud.processor

import com.codahale.metrics.MetricRegistry
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.Reactor

@CompileStatic
@Configuration
class ProcessorConfiguration {

    @Bean
    OkClientProcessor okClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        return new OkClientProcessor(reactor, serviceRestClient, new RequestBodyBuilder())
    }

    @Bean
    FishyClientProcessor fishyClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        return new FishyClientProcessor(reactor, serviceRestClient, new RequestBodyBuilder())
    }

    @Bean
    FraudClientProcessor fraudClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        return new FraudClientProcessor(reactor, serviceRestClient, new RequestBodyBuilder())
    }

    @Bean
    ProcessingTimeMetricAspect processingTimeMetricAspect(MetricRegistry metricRegistry) {
        return new ProcessingTimeMetricAspect(metricRegistry)
    }
}
