package net.arvandor.ticketbot.discord

import net.arvandor.ticketbot.trello.TrelloService
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ModalInteractionListener(val trelloAuth: String, val trello: TrelloService) : ListenerAdapter() {

    override fun onModalInteraction(event: ModalInteractionEvent) {
        when (event.modalId) {
            "ticket" -> {
                val title = event.getValue("title")?.asString
                val description = event.getValue("description")?.asString

                val response = trello.createCard(trelloAuth, "653546dd620f5827dda6be5a", title, description, "top").execute()
                if (response.isSuccessful) {
                    event.reply("Ticket created. Thanks for your feedback!")
                        .setEphemeral(true)
                        .queue()
                } else {
                    println(response.errorBody()?.string())
                    event.reply("Something went wrong. Please try again later.")
                        .setEphemeral(true)
                        .queue()
                }
            }
        }
    }

}