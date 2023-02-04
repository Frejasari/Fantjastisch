package de.fantjastisch.cards_frontend.util


/**
 * Fügt ein Semikolon gefolgt mit einem Leerzeichen am Ende eines Strings hinzu
 *
 * @return der formatierte String
 */
fun String.formatToInlineLabel() : String {
    return String.format("%s: ", this)
}