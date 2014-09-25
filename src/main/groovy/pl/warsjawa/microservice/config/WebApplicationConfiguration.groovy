package pl.warsjawa.microservice.config
import com.ofg.infrastructure.config.WebAppConfiguration
import com.ofg.infrastructure.reactor.ReactorInfraConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import([WebAppConfiguration, ReactorInfraConfiguration])
class WebApplicationConfiguration {

}
