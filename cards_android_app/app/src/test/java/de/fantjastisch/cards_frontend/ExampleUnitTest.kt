package de.fantjastisch.cards_frontend

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test
import org.openapitools.client.models.ErrorEntryEntity
import org.openapitools.client.models.ErrorResponseEntity

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val type =
            Types.newParameterizedType(
//                List::class.java,
                ErrorResponseEntity::class.java,
                ErrorEntryEntity::class.java
            )
        val adapter: JsonAdapter<ErrorResponseEntity> = moshi.adapter(type)
//                    val adapter: JsonAdapter<ErrorResponseEntity> =
//                        moshi.adapter(ErrorResponseEntity::class.java)

        val errorBody =
            "{\"errors\":[{\"code\":\"NOT_BLANK_VIOLATION\",\"field\":\"question\"},{\"code\":\"NOT_BLANK_VIOLATION\",\"field\":\"tag\"},{\"code\":\"NOT_BLANK_VIOLATION\",\"field\":\"answer\"},{\"code\":\"CONSTRAINT_VIOLATION\",\"field\":\"categories\"}]}"
        val errors: ErrorResponseEntity? = adapter.fromJson(errorBody)
        errors
    }
}