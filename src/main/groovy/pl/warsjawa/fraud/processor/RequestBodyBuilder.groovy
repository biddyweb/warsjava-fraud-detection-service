package pl.warsjawa.fraud.processor

import groovy.json.JsonSlurper
import groovy.transform.PackageScope
import pl.warsjawa.fraud.worker.FraudResult

@PackageScope
class RequestBodyBuilder {

    String buildDecisionRequestBody(String loanApplicationDetails, FraudResult fraudResult) {
        def root = new JsonSlurper().parseText(loanApplicationDetails)

        def builder = new groovy.json.JsonBuilder()
        builder {
            fraudStatus fraudResult.jobFraudResult
            job root.job
            amount root.amount
        }

        return builder.toString()
    }

}
