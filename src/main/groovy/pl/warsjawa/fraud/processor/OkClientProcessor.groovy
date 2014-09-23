package pl.warsjawa.fraud.processor

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import pl.warsjawa.fraud.Dependencies
import pl.warsjawa.fraud.worker.FraudResult
import reactor.core.Reactor
import reactor.event.Event
import reactor.spring.annotation.Selector

import static pl.warsjawa.fraud.FraudApi.DECISION_MAKER_V1
import static pl.warsjawa.fraud.events.FraudEvents.CLIENT_IS_OK

@Slf4j
@CompileStatic
@PackageScope
class OkClientProcessor implements BodyBuilding, ClientProcessing {

    final Reactor reactor
    final ServiceRestClient serviceRestClient

    OkClientProcessor(Reactor reactor, ServiceRestClient serviceRestClient) {
        this.reactor = reactor
        this.serviceRestClient = serviceRestClient
    }

    @Override
    @Selector(CLIENT_IS_OK)
    void subscribeForFraudEvent(Event<Map<String, String>> event) {
        Map<String, String> data = event.data
        String loanApplicationId = data['loanApplicationId']
        log.info("Sending a request to [$Dependencies.DECISION_MAKER] to decide whether to grant a loan for loan application id [$loanApplicationId]")
        log.debug("Original loanApplicationDetails were: [${data['loanApplicationDetails']}]")
        serviceRestClient.forService(Dependencies.DECISION_MAKER.toString())
                .put()
                .onUrl("/api/loanApplication/$loanApplicationId")
                .body(buildBody(new FraudResult(FraudResult.JobFraudResult.OK)))
                .withHeaders()
                .contentType(DECISION_MAKER_V1)
                .andExecuteFor()
                .ignoringResponse()
    }

    /**
     * Reactor needs to have this field accessible explicitly
     */
    Reactor getReactor() {
        return reactor
    }
}
