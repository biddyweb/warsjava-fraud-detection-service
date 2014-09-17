package pl.warsjawa.fraud.reactor

import groovy.transform.CompileStatic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@CompileStatic
@Configuration
class ReactorAspectConfiguration {

    @Bean ReactorAspect reactorAspect() {
        return new ReactorAspect()
    }
}
