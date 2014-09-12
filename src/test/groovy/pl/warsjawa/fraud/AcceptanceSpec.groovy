package pl.warsjawa.fraud
import pl.warsjawa.base.MicroserviceMvcWiremockSpec
import spock.lang.Unroll

import static com.jayway.awaitility.Awaitility.await
import static java.util.concurrent.TimeUnit.SECONDS
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AcceptanceSpec extends MicroserviceMvcWiremockSpec {

    @Unroll
    def "should mark job position as [#fraudResult] for a [#job] job"() {
        given: "a loan application with job position 'IT'"
            String request = Jsons.IT_LOAN_APPLICATION
        when: 'performing fraud detection'
        mockMvc.perform(put(Jsons.IT_LOAN_APPLICATION_ID).contentType(TWITTER_PLACES_ANALYZER_MICROSERVICE_V1).content("[$tweet]"))
                .andExpect(status().isOk())
        then: 'loan application should pass the job position check'
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
                                                                        ''')))})
        where:
            job                 || fraudResult
            'IT'                || 'OK'
            'SOME CUSTOM VALUE' || 'FRAUD'
            'FINANCE SECTOR'    || 'VERIFICATION_REQUIRED'
    }
    


}
