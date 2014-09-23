package pl.warsjawa.fraud.worker
import groovy.transform.CompileStatic

@CompileStatic
class FraudResult {

    final JobFraudResult jobFraudResult

    FraudResult(JobFraudResult jobFraudResult) {
        this.jobFraudResult = jobFraudResult
    }

    FraudResult(FraudResult fraudResult) {
        this.jobFraudResult = fraudResult.jobFraudResult
    }

    static enum JobFraudResult {
        OK, FRAUD, VERIFICATION_REQUIRED
    }
}

