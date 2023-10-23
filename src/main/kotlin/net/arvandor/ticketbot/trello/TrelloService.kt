package net.arvandor.ticketbot.trello

import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface TrelloService {
    @POST("cards")
    fun createCard(
        @Header("Authorization") auth: String,
        @Query("idList") idList: String,
        @Query("name") name: String?,
        @Query("desc") desc: String?,
        @Query("pos") pos: String?,
    ): Call<TrelloCardCreateResponse>
}