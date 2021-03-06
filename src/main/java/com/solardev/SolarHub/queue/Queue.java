package com.solardev.SolarHub.queue;

import com.solardev.SolarHub.SolarHub;
import com.solardev.SolarHub.utils.BungeeUtils;
import com.solardev.SolarHub.utils.QueueUtils;
import com.solardev.SolarHub.utils.chat.Color;
import com.solardev.SolarHub.utils.runnable.Runnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Queue {

    private SolarHub plugin;
    private final HashMap<String, Player> queue = new HashMap<>();
    public static ArrayList<String> queues = new ArrayList<>();


    public Player getPlayer(SolarHub plugin, Player player, String server) {
        return queue.get(server);
    }

    public String getServer(SolarHub plugin, String server, Player player) {
        return queue.get(server).toString();
    }

    public static void createQueue(SolarHub plugin, String server, Player player) {
        List<String> queues = Collections.singletonList(plugin.getConfig().getString("servers." + server + "bungeecord-name"));
        for (String queueString : queues) {
            if (Queue.queues.contains(server)) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + server + " is already created and registered as queue");
                return;
            }else {
                Queue.queues.add(server);
            }

        }
    }

    public boolean isQueueCreated(SolarHub plugin, String server) {
        return queues.contains(server);
    }

    public void getQueue(SolarHub plugin, String server) {
        if (isQueueCreated(plugin, server)) {
            ArrayList<Player> queue1 = new ArrayList<>();
        }else {
            return;
        }
    }

    public void startQueue(Player player, String server) {
        List<Player> queue1 = QueueUtils.queue1;
        Bukkit.getScheduler().scheduleSyncDelayedTask(SolarHub.getINSTANCE(), new java.lang.Runnable() {
            @Override
            public void run() {
                if (queue1.get(0).getPlayer() != null) {
                    Player pos1 = queue1.get(0);
                    String sendingMessage = plugin.getLang().getString("send-to-server").replaceAll("<server>", server);
                    pos1.sendMessage(Color.translate(sendingMessage));
                    queue1.remove(pos1);
                    plugin.setMainScoreboard(pos1);
                    BungeeUtils.send(pos1, plugin.getConfig().getString("servers." + server + ".bungeecord-name"));
                }
            }
        }, plugin.getConfig().getLong("queue.delay"));
    }

    public void sendMessage(Player player, String server) {
        List<Player> queue1 = QueueUtils.queue1;
        int urPos = queue1.lastIndexOf(player) + 1;
        Runnable.scheduleSyncDelayedTask(SolarHub.getINSTANCE(), new BukkitRunnable() {
            @Override
            public void run() {
                if (!queue1.contains(player)) {
                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                }else {
                    if (urPos == 0 && queue1.size() == 1) {
                        queue1.remove(player);
                        Bukkit.getScheduler().cancelTask(this.getTaskId());
                    }else {
                        if (!(queue1.lastIndexOf(player) == 0) && queue1.size() == 0) {
                            queue1.remove(player);
                            Bukkit.getScheduler().cancelTask(this.getTaskId());
                        }else {
                            int urPos1 = urPos - 1;
                            if (queue1.lastIndexOf(player) == 0) {
                                player.sendMessage(Color.translate(plugin.getConfig().getString("queue.message").replaceAll("<pos>", String.valueOf(urPos) + "#").replaceAll("<total>", queue1.size() + "#")));
                            }else {
                                player.sendMessage(Color.translate(plugin.getConfig().getString("queue.message").replaceAll("<pos>", urPos + "#").replaceAll("<total>", String.valueOf(queue1.size()) + "#")));
                            }
                        }
                    }
                }
            }
        }, plugin.getConfig().getLong("queue.delay"));
    }
}
