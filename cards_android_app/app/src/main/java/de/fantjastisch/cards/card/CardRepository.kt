package de.fantjastisch.cards.card

import androidx.room.*

class CardRepository(private val dao: CardDao): CardDao by dao

@Dao
interface CardDao {
    @Query("SELECT id, answer, question FROM cards")
    fun getAll(): List<Card>

    @Query("SELECT c.id, c.answer, c.question FROM cards c WHERE id=:id")
    fun findById(id: String): Card

    @Insert
    fun insert(card: Card)

    @Query("delete from cards where id=:id")
    fun delete(id: String)
}