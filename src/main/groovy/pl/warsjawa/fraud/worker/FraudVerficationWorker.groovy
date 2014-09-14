package pl.warsjawa.fraud.worker
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import pl.warsjawa.fraud.Dependencies

import static pl.warsjawa.fraud.FraudApi.DECISION_MAKER_V1

@CompileStatic
@Slf4j
@PackageScope
class FraudVerficationWorker implements PropagationWorker {

    private final ServiceRestClient serviceRestClient
    private final FraudResultBuilder fraudResultBuilder

    FraudVerficationWorker(ServiceRestClient serviceRestClient, FraudResultBuilder fraudResultBuilder) {
        this.serviceRestClient = serviceRestClient
        this.fraudResultBuilder = fraudResultBuilder
    }

    @Override
    void checkAndPropagate(String loanApplicationId, String loanApplicationDetails) {
        FraudResult fraudResult = fraudResultBuilder.buildFraudResult(loanApplicationDetails)
        log.info("Sending a request to [$Dependencies.DECISION_MAKER] to decide whether to grant a loan")
        serviceRestClient.forService(Dependencies.DECISION_MAKER.toString())
                .put()
                .onUrl("/api/loanApplication/$loanApplicationId")
                .body(buildBody(fraudResult))
                .withHeaders()
                    .contentType(DECISION_MAKER_V1)
                .andExecuteFor()
                .ignoringResponse()
    }

    private String buildBody(FraudResult fraudResult) {
        return """{
                    "fraudResult" : {
                        "job":"${fraudResult.jobFraudResult.toString()}"
                   }
                }"""
    }
}
