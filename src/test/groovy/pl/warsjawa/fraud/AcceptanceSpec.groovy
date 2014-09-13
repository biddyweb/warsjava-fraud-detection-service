package pl.warsjawa.fraud
import org.springframework.http.MediaType
import pl.warsjawa.base.MicroserviceMvcWiremockSpec
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.warsjawa.fraud.FraudApi.API_URL
import static pl.warsjawa.fraud.FraudApi.FRAUD_JSON_V1
import static pl.warsjawa.fraud.FraudApi.LOAN_APPLICATION_ROOT_URL
import static pl.warsjawa.fraud.Jsons.IT_LOAN_APPLICATION
import static pl.warsjawa.fraud.Jsons.IT_LOAN_APPLICATION_ID

class AcceptanceSpec extends MicroserviceMvcWiremockSpec {

    public static final MediaType FRAUD_DETECTION_SERVICE_V1 = new MediaType('application', FRAUD_JSON_V1)
    public static final String LOAN_DECISION_MAKER_ENDPOINT_URL = '/decision-maker'
    //public static final UrlMatchingStrategy COLLERATOR_URL_WITH_PAIR_ID = urlEqualTo("$COLLERATOR_ENPOINT_URL/$PAIR_ID")

    @Unroll
    def "should mark job position as [#fraudResult] for a [#job] job"() {
        given: "a loan application with job position 'IT'"
            String loanApplication = IT_LOAN_APPLICATION
        expect: 'performing fraud detection'
            mockMvc.perform(put("$API_URL/$LOAN_APPLICATION_ROOT_URL/$IT_LOAN_APPLICATION_ID").contentType(FRAUD_DETECTION_SERVICE_V1).content(loanApplication))
                .andExpect(status().isOk())
        /*then: 'loan application should pass the job position check'
            await().atMost(2, SECONDS).until({ wireMock.verifyThat(postRequestedFor(COLLERATOR_URL_WITH_PAIR_ID).withRequestBody(equalToJson('''
                                                                        [{
                                                                            "pair_id" : 1,
                                                                            "tweet_id" : "492967299297845248",
                                                                            "place" :
                                                                            {
                                                                                "name":"Washington",
                                                                                "country_code": "US"
                                                                            },
                                                                            "probability" : "2",
                                                                            "origin" : "twitter_place_section"
                                                                        }]
                                                                        ''')))})*/
        where:
            job                 || fraudResult
            'IT'                || 'OK'
            'SOME CUSTOM VALUE' || 'FRAUD'
            'FINANCE SECTOR'    || 'VERIFICATION_REQUIRED'
    }
    


}
