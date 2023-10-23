package net.arvandor.ticketbot

import net.arvandor.ticketbot.discord.ModalInteractionListener
import net.arvandor.ticketbot.discord.SlashCommandInteractionListener
import net.arvandor.ticketbot.trello.TrelloService
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.requests.GatewayIntent.AUTO_MODERATION_CONFIGURATION
import net.dv8tion.jda.api.requests.GatewayIntent.AUTO_MODERATION_EXECUTION
import net.dv8tion.jda.api.requests.GatewayIntent.DIRECT_MESSAGE_TYPING
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_EMOJIS_AND_STICKERS
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_INVITES
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGE_TYPING
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MODERATION
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_PRESENCES
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES
import net.dv8tion.jda.api.requests.GatewayIntent.GUILD_WEBHOOKS
import net.dv8tion.jda.api.utils.cache.CacheFlag.ACTIVITY
import net.dv8tion.jda.api.utils.cache.CacheFlag.CLIENT_STATUS
import net.dv8tion.jda.api.utils.cache.CacheFlag.EMOJI
import net.dv8tion.jda.api.utils.cache.CacheFlag.FORUM_TAGS
import net.dv8tion.jda.api.utils.cache.CacheFlag.MEMBER_OVERRIDES
import net.dv8tion.jda.api.utils.cache.CacheFlag.SCHEDULED_EVENTS
import net.dv8tion.jda.api.utils.cache.CacheFlag.STICKER
import net.dv8tion.jda.api.utils.cache.CacheFlag.VOICE_STATE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TicketBot {
    val discordToken = System.getenv("DISCORD_TOKEN")
    val trelloKey = System.getenv("TRELLO_KEY")
    val trelloToken = System.getenv("TRELLO_TOKEN")
    val trelloAuthHeader = "OAuth oauth_consumer_key=\"${trelloKey}\", oauth_token=\"${trelloToken}\""

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.trello.com/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val trello = retrofit.create(TrelloService::class.java)

    val jda = JDABuilder.createDefault(discordToken)
        .disableCache(
            ACTIVITY,
            VOICE_STATE,
            EMOJI,
            STICKER,
            CLIENT_STATUS,
            MEMBER_OVERRIDES,
            FORUM_TAGS,
            SCHEDULED_EVENTS
        )
        .setActivity(Activity.listening("to your feedback"))
        .disableIntents(
            GUILD_MODERATION,
            GUILD_EMOJIS_AND_STICKERS,
            GUILD_WEBHOOKS,
            GUILD_INVITES,
            GUILD_VOICE_STATES,
            GUILD_PRESENCES,
            GUILD_MESSAGE_TYPING,
            DIRECT_MESSAGE_TYPING,
            GatewayIntent.SCHEDULED_EVENTS,
            AUTO_MODERATION_CONFIGURATION,
            AUTO_MODERATION_EXECUTION
        )
        .setLargeThreshold(50)
        .addEventListeners(
            SlashCommandInteractionListener(),
            ModalInteractionListener(
                trelloAuthHeader,
                trello
            )
        )
        .build()

    init {
        jda.awaitReady()
        val guild = jda.getGuildById(1074780931220586546)
        if (guild == null) {
            println("guild not found")
        } else {
            guild.upsertCommand(
                Commands.slash("ticket", "Create a ticket on Trello")
            ).queue()
        }
    }

}

fun main() {
    TicketBot()
}