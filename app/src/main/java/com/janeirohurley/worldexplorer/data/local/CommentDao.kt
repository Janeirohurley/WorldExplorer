package com.janeirohurley.worldexplorer.data.local
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Query("SELECT * FROM comments WHERE countryName = :countryName ORDER BY id DESC")
    fun getCommentsForCountry(countryName: String): Flow<List<Comment>>

    @Query("SELECT * FROM comments WHERE countryName = :countryName")
    suspend fun getCommentsForCountryOnce(countryName: String): List<Comment>

}
