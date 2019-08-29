package me.xcodiq.crypticshards.conversations.factory;

import me.xcodiq.crypticshards.Core;
import me.xcodiq.crypticshards.utilities.ChatUtils;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class ShardConversation {

    private ConversationFactory conversationFactory = new ConversationFactory(Core.getInstance());
    private Conversation conversation;

    public ShardConversation(Player player, Prompt prompt, String exitString) {
        conversation = conversationFactory
                .withFirstPrompt(prompt)
                .withEscapeSequence(exitString)
                .withLocalEcho(false)
                .buildConversation(player);

        conversation.addConversationAbandonedListener(event -> {
            if (event.gracefulExit()) {
                return;
            }
            player.sendMessage(ChatUtils.format(Core.getInstance().getConfig().getString("messages.cancel-conversation")));
        });
    }

    public void start() {
        conversation.begin();
    }

    public void stop() {
        conversation.abandon();
    }
}
