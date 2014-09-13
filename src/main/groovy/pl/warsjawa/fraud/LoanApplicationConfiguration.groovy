package pl.warsjawa.fraud

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LoanApplicationConfiguration {

    @Bean
    LoanApplicationController loanApplicationController() {
        return new LoanApplicationController()
    }
}
