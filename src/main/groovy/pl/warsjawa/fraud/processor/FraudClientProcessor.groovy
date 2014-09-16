package pl.warsjawa.fraud.processor

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import pl.warsjawa.fraud.Dependencies
import pl.warsjawa.fraud.events.FraudEvents
import pl.warsjawa.fraud.worker.FraudResult
import reactor.core.Reactor
import reactor.event.Event

import static pl.warsjawa.fraud.FraudApi.DECISION_MAKER_V1
import static reactor.event.selector.Selectors.$

@Slf4j
@CompileStatic
@PackageScope
class FraudClientProcessor implements FraudEventSubscribable, BodyBuilding {
    private final Reactor reactor
    private final ServiceRestClient serviceRestClient

    FraudClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        this.reactor = reactor
        this.serviceRestClient = serviceRestClient
    }

    @Override
    void subscribeForFraudEvent() {
        this.reactor.on($(FraudEvents.CLIENT_IS_FRAUD)) { Event<Map<String, String>> event ->
            Map<String, String> data = event.data
            String loanApplicationId = data['loanApplicationId']
            log.info("Sending a request to [$Dependencies.DECISION_MAKER] to decide whether to grant a loan for loan application id []")
            log.debug("Original loanApplicationDetails were: [${data['loanApplicationDetails']}]")
            serviceRestClient.forService(Dependencies.DECISION_MAKER.toString())
                    .put()
                    .onUrl("/api/loanApplication/$loanApplicationId")
                    .body(buildBody(new FraudResult(FraudResult.JobFraudResult.FRAUD)))
                    .withHeaders()
                    .contentType(DECISION_MAKER_V1)
                    .andExecuteFor()
                    .ignoringResponse()
        }
    }
}
