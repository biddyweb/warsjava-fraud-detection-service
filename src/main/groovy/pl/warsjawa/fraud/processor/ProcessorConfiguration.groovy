package pl.warsjawa.fraud.processor

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.Reactor

@CompileStatic
@Configuration
class ProcessorConfiguration {

    @Bean(initMethod = 'subscribeForFraudEvent')
    OkClientProcessor okClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        return new OkClientProcessor(reactor, serviceRestClient)
    }

    @Bean(initMethod = 'subscribeForFraudEvent')
    FishyClientProcessor fishyClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        return new FishyClientProcessor(reactor, serviceRestClient)
    }

    @Bean(initMethod = 'subscribeForFraudEvent')
    FraudClientProcessor fraudClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        return new FraudClientProcessor(reactor, serviceRestClient)
    }
}
