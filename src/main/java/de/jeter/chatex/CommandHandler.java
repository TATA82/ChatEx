package de.jeter.chatex;

import de.jeter.chatex.utils.Config;
import de.jeter.chatex.utils.HookManager;
import de.jeter.chatex.utils.Locales;
import de.jeter.chatex.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§aChatEx plugin by " + ChatEx.getInstance().getDescription().getAuthors());
            return true;
        } else if (args.length > 1) {
            sender.sendMessage(Locales.COMMAND_RESULT_WRONG_USAGE.getString(null).replaceAll("%cmd", command.getName()));
            return true;
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("chatex.reload")) {
                    Bukkit.getPluginManager().disablePlugin(ChatEx.getInstance());
                    Bukkit.getPluginManager().enablePlugin(ChatEx.getInstance());
                    sender.sendMessage(Locales.MESSAGES_RELOAD.getString(null));

                    if (Config.CHANGE_TABLIST_NAME.getBoolean()) {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            String name = Config.TABLIST_FORMAT.getString();

                            if (HookManager.checkPlaceholderAPI()) {
                                name = PlaceholderAPI.setPlaceholders(p, name);
                            }

                            name = Utils.replacePlayerPlaceholders(p, name);

                            p.setPlayerListName(name);
                        }
                    }
                    return true;
                } else {
                    sender.sendMessage(Locales.COMMAND_RESULT_NO_PERM.getString(null).replaceAll("%perm", "chatex.reload"));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("clear")) {
                if (sender.hasPermission("chatex.clear")) {
                    for (int i = 0; i < 50; i++) {
                        Bukkit.broadcastMessage("\n");
                    }

                    Player clearer = null;

                    String who = Locales.COMMAND_CLEAR_UNKNOWN.getString(null);
                    if ((sender instanceof ConsoleCommandSender) || (sender instanceof BlockCommandSender)) {
                        who = Locales.COMMAND_CLEAR_CONSOLE.getString(null);
                    } else if (sender instanceof Player) {
                        who = sender.getName();
                        clearer = (Player) sender;
                    }
                    Bukkit.broadcastMessage(Locales.MESSAGES_CLEAR.getString(clearer) + who);
                    return true;
                } else {
                    sender.sendMessage(Locales.COMMAND_RESULT_NO_PERM.getString(null).replaceAll("%perm", "chatex.clear"));
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                sender.sendMessage("§a/" + command.getName() + " reload - " + Locales.COMMAND_RELOAD_DESCRIPTION.getString(null));
                sender.sendMessage("§a/" + command.getName() + " clear - " + Locales.COMMAND_CLEAR_DESCRIPTION.getString(null));
                return true;
            } else {
                sender.sendMessage(Locales.COMMAND_RESULT_WRONG_USAGE.getString(null).replaceAll("%cmd", "/chatex"));
                return true;
            }
        }

    }
}
