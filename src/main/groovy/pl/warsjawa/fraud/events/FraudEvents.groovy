package pl.warsjawa.fraud.events

import groovy.transform.CompileStatic

@CompileStatic
class FraudEvents {

    public static final String CLIENT_IS_OK = 'clientOk'
    public static final String CLIENT_IS_FISHY = 'clientFishy'
    public static final String CLIENT_IS_FRAUD = 'clientFraud'
}
