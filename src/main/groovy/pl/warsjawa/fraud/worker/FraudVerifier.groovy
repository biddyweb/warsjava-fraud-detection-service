package pl.warsjawa.fraud.worker
import groovy.json.JsonSlurper
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import pl.warsjawa.fraud.events.FraudEvents
import reactor.core.Reactor

import static pl.warsjawa.fraud.events.FraudEvents.*

@PackageScope
@Slf4j
class FraudVerifier {

    private final Reactor reactor

    FraudVerifier(Reactor reactor) {
        this.reactor = reactor
    }

    void checkIfApplicationIsFraud(String loanApplicationId, String loanApplicationDetails) {
        def root = new JsonSlurper().parseText(loanApplicationDetails)
        String job = root.job.toLowerCase()
        log.debug("Checking job [$job]")
        reactor.notify(for: eventNameBasedOnJob(job), data: [loanApplicationDetails: loanApplicationDetails, loanApplicationId: loanApplicationId])
    }

    private FraudEvents eventNameBasedOnJob(String job) {
        switch (job) {
            case 'it':
                return CLIENT_IS_OK
            case 'finance_sector':
                return CLIENT_IS_FISHY
            default:
                return CLIENT_IS_FRAUD
        }
    }
}
