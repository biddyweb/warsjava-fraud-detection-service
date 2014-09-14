package pl.warsjawa.fraud

import com.wordnik.swagger.annotations.Api
import com.wordnik.swagger.annotations.ApiOperation
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import pl.warsjawa.fraud.worker.PropagationWorker

import javax.validation.constraints.NotNull
import java.util.concurrent.Callable

import static pl.warsjawa.fraud.FraudApi.*

@CompileStatic
@Slf4j
@RestController
@RequestMapping(API_URL)
@Api(value = "fraudDetection", description = "Checks loan application details for possible fraud cases")
class LoanApplicationController {

    private final PropagationWorker propagationWorker

    @Autowired
    LoanApplicationController(PropagationWorker propagationWorker) {
        this.propagationWorker = propagationWorker
    }

    @RequestMapping(
            value = LOAN_APPLICATION_URL,
            method = RequestMethod.PUT,
            consumes = API_VERSION_1,
            produces = API_VERSION_1)
    @ApiOperation(value = "Async verification of loan application by given loanApplicationId",
            notes = "This will asynchronously verify what's the probability of the user to be a fraud and will call LoanApplicationDecisionMaker")
    Callable<Void> checkIfUserIsFraud(@PathVariable @NotNull String loanApplicationId, @RequestBody @NotNull String loanApplicationDetails) {
        return {
            propagationWorker.checkAndPropagate(loanApplicationId, loanApplicationDetails)
        }
    }
}
