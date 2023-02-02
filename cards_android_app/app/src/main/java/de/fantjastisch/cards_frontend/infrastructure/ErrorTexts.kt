package de.fantjastisch.cards_frontend.infrastructure

import de.fantjastisch.cards.R

enum class ErrorTexts(val text: Int) {
    NO_ERROR(0),
    LINK_ERROR(R.string.error_link_no_card),
    NETWORK(R.string.error_network),
    UNEXPECTED(R.string.error_unexpected)
}