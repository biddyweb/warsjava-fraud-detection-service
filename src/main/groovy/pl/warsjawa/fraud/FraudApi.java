package pl.warsjawa.fraud;

public class FraudApi {
    public static final String API_URL = "/api" ;
    public static final String APPLICATION = "application";
    public static final String FRAUD_NAME = "vnd.pl.warsjawa.fraud-detection-service";
    public static final String JSON_V1 = ".v1+json";
    public static final String FRAUD_JSON_V1 = FRAUD_NAME + JSON_V1;

    public static final String API_VERSION_1 = APPLICATION + "/" + FRAUD_JSON_V1;
    public static final String LOAN_APPLICATION_ROOT_URL = "loanApplication";
    public static final String LOAN_APPLICATION_URL = LOAN_APPLICATION_ROOT_URL + "/{loanApplicationId}";
}
