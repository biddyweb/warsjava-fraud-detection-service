package pl.warsjawa.fraud
import com.github.tomakehurst.wiremock.client.UrlMatchingStrategy
import org.springframework.http.MediaType
import pl.warsjawa.base.MicroserviceMvcWiremockSpec
import spock.lang.Ignore
import spock.lang.Unroll

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.jayway.awaitility.Awaitility.await
import static java.util.concurrent.TimeUnit.SECONDS
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.warsjawa.fraud.Dependencies.DECISION_MAKER
import static pl.warsjawa.fraud.FraudApi.*

@Ignore
class AcceptanceSpec extends MicroserviceMvcWiremockSpec {

    public static final String IT_LOAN_APPLICATION_ID = '123123123123'
    public static final MediaType FRAUD_DETECTION_SERVICE_V1 = new MediaType('application', FRAUD_JSON_V1)
    public static final String LOAN_DECISION_MAKER_ENDPOINT_URL = "/${DECISION_MAKER.toString()}"
    public static final UrlMatchingStrategy LOAN_DECISION_MAKER_WITH_LOAN_APP_ID = urlEqualTo("$LOAN_DECISION_MAKER_ENDPOINT_URL/$LOAN_APPLICATION_ROOT_URL/$IT_LOAN_APPLICATION_ID")

    @Unroll
    def "should mark job position as [#fraudResult] for a [#job] job"() {
        given: "a loan application with job position 'IT'"
            String loanApplication = """{
                                            "name":"John",
                                            "surname":"Doe",
                                            "age":20,
                                            "amount":2000,
                                            "job":"$job"
                                        }
                                    """
        when: 'performing fraud detection'
            mockMvc.perform(put("$API_URL/$LOAN_APPLICATION_ROOT_URL/$IT_LOAN_APPLICATION_ID").contentType(FRAUD_DETECTION_SERVICE_V1).content(loanApplication))
                .andExpect(status().isOk())
        then: 'loan application should pass the job position check'
            await().atMost(2, SECONDS).until({ wireMock.verifyThat(putRequestedFor(LOAN_DECISION_MAKER_WITH_LOAN_APP_ID).withRequestBody(equalToJson("""
                                                                        {
                                                                           "fraudResult" : {
                                                                            "job":"$fraudResult"
                                                                           }
                                                                        }
                                                                        """)))})
        where:
            job                 || fraudResult
            'IT'                || 'OK'
            'SOME CUSTOM VALUE' || 'FRAUD'
            'FINANCE_SECTOR'    || 'VERIFICATION_REQUIRED'
    }
    


}
