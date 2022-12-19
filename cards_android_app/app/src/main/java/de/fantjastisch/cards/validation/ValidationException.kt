package de.fantjastisch.cards.validation

class ValidationException(val errors: List<String>) : Throwable() {

}
