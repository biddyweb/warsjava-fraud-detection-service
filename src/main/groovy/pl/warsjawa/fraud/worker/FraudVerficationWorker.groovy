package pl.warsjawa.fraud.worker

import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j

@CompileStatic
@Slf4j
@PackageScope
class FraudVerficationWorker implements PropagationWorker {
    private final FraudVerifier fraudVerifier

    FraudVerficationWorker(FraudVerifier fraudVerifier) {
        this.fraudVerifier = fraudVerifier
    }

    @Override
    void checkAndPropagate(String loanApplicationId, String loanApplicationDetails) {
        fraudVerifier.checkIfApplicationIsFraud(loanApplicationId, loanApplicationDetails)
    }

}
