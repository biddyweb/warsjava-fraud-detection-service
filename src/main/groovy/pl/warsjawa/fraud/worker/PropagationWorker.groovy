package pl.warsjawa.fraud.worker

interface PropagationWorker {
    void checkAndPropagate(String loanApplicationId, String loanApplicationDetails)
}