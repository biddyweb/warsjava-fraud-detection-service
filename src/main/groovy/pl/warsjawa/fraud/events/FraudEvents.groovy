package pl.warsjawa.fraud.events

import groovy.transform.CompileStatic

@CompileStatic
enum FraudEvents {
    CLIENT_IS_OK,
    CLIENT_IS_FISHY,
    CLIENT_IS_FRAUD
}
