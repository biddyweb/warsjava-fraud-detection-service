package pl.warsjawa.fraud.worker

import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.Reactor

@Configuration
@CompileStatic
class VerificationConfiguration {

    @Bean
    PropagationWorker propagationWorker(Reactor reactor) {
        return new FraudVerficationWorker(new FraudVerifier(reactor))
    }
}
