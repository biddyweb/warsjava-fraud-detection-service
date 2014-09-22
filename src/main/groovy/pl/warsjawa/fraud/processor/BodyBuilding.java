package pl.warsjawa.fraud.processor;

import pl.warsjawa.fraud.worker.FraudResult;

public interface BodyBuilding {
    default String buildBody(FraudResult fraudResult) {
        return "{'fraudResult':{'job':'" + fraudResult.getJobFraudResult().toString() + "'}}";
    }
}
