package net.arvandor.ticketbot.discord

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle.PARAGRAPH
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle.SHORT
import net.dv8tion.jda.api.interactions.modals.Modal

class SlashCommandInteractionListener : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "ticket" -> {
                val title = TextInput.create("title", "Title", SHORT)
                    .setPlaceholder("Title")
                    .setMinLength(1)
                    .build()

                val description = TextInput.create("description", "Description", PARAGRAPH)
                    .setPlaceholder("Description")
                    .build()

                val modal = Modal.create("ticket", "Create ticket")
                    .addComponents(ActionRow.of(title), ActionRow.of(description))
                    .build()

                event.replyModal(modal).queue()
            }
        }
    }
}