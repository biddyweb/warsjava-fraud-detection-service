package pl.warsjawa.fraud

import pl.warsjawa.base.MicroserviceMvcWiremockSpec

class AcceptanceSpec extends MicroserviceMvcWiremockSpec {
           
    def "should mark job position as [#fraudResult] for a [#job] job"() {
        given: "a loan application with job position 'IT'"
        when: 'performing fraud detection'
        then: 'loan application should pass the job position check'
        where:
            job || fraudResult
            'IT'|| 'OK'
            'SOME CUSTOM VALUE'|| 'FRAUD'
            'SOME CUSTOM VALUE'|| 'FRAUD'
    }
    


}
