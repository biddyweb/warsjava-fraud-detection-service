package pl.warsjawa.fraud.worker

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@CompileStatic
class VerificationConfiguration {

    @Bean
    PropagationWorker propagationWorker(ServiceRestClient serviceRestClient) {
        return new FraudVerficationWorker(serviceRestClient, new FraudResultBuilder())
    }
}
