package pl.warsjawa.fraud.processor

import reactor.event.Event

interface ClientProcessing {
    void subscribeForFraudEvent(Event<Map<String, String>> event)
}