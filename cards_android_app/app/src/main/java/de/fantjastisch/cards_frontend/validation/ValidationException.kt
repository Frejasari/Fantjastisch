package de.fantjastisch.cards_frontend.validation

class ValidationException(val errors: List<String>) : Throwable() {

}
