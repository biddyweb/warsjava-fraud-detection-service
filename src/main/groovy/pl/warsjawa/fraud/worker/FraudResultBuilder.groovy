package pl.warsjawa.fraud.worker

import groovy.json.JsonSlurper
import groovy.transform.PackageScope
import groovy.util.logging.Slf4j

import static pl.warsjawa.fraud.worker.FraudResult.JobFraudResult.FRAUD
import static pl.warsjawa.fraud.worker.FraudResult.JobFraudResult.OK
import static pl.warsjawa.fraud.worker.FraudResult.JobFraudResult.VERIFICATION_REQUIRED

@PackageScope
@Slf4j
class FraudResultBuilder {

    FraudResult buildFraudResult(String loanApplicationDetails) {
        def root = new JsonSlurper().parseText(loanApplicationDetails)
        return jobFraudResultFrom(root)
    }

    FraudResult jobFraudResultFrom(def root) {
        String job = root.job.toLowerCase()
        log.debug("Checking job [$job]")
        switch(job) {
            case 'it':
                return new FraudResult(OK)
            case 'finance_sector':
                return new FraudResult(VERIFICATION_REQUIRED)
            default:
                return new FraudResult(FRAUD)
        }
    }
}
