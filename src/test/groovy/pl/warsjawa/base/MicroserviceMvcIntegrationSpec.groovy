package pl.warsjawa.base
import com.ofg.infrastructure.base.MvcIntegrationSpec
import pl.warsjawa.microservice.Application
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration(classes = [Application], loader = SpringApplicationContextLoader)
class MicroserviceMvcIntegrationSpec extends MvcIntegrationSpec {


}
