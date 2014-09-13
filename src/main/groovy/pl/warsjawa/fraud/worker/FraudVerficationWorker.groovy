package pl.warsjawa.fraud.worker

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import pl.warsjawa.fraud.Dependencies

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
        serviceRestClient.forService(Dependencies.DECISION_MAKER.toString())
                .put()
                .onUrl("loanApplication/$loanApplicationId")
                .body(buildBody(fraudResult))
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
