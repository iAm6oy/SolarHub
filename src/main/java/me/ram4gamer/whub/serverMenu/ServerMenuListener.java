package me.ram4gamer.whub.serverMenu;

import me.ram4gamer.whub.Manager.PriorityManager;
import me.ram4gamer.whub.Manager.QueueManager;
import me.ram4gamer.whub.WHub;
import me.ram4gamer.whub.utils.BungeeUtils;
import me.ram4gamer.whub.utils.chat.Color;
import me.ram4gamer.whub.utils.runnable.Runnable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Queue;

public class ServerMenuListener implements Listener {

    private WHub plugin;

    public ServerMenuListener(WHub plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void on(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_AQUA + "Server Selector")) {
            if (e.getCurrentItem() != null) {
                e.setCancelled(true);
                if (e.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
                    if (p.hasPermission("hub.queue.priority")) {
                        int peopleinPriorityQueue = PriorityManager.vipPracticQueue.size();
                        p.closeInventory();
                        PriorityManager.inserttovipPracticeQueue(p, peopleinPriorityQueue);
                        plugin.setInVipQueueScoreboard(p);
                        int urPos = PriorityManager.vipPracticQueue.lastIndexOf(p) + 1;
                        p.sendMessage(Color.translate("&bThanks for buying a rank on our Server, now you will be sent to Practice"));
                        p.sendMessage(Color.translate("&32x&b Faster!"));
                        //Getting 1st place & Sending them to Practice
                        Bukkit.getScheduler().scheduleSyncDelayedTask(WHub.getINSTANCE(), new java.lang.Runnable() {
                            @Override
                            public void run() {
                                if (PriorityManager.vipPracticQueue.get(0).getPlayer() != null) {
                                    Player pos1 = PriorityManager.vipPracticQueue.get(0);
                                    pos1.sendMessage(Color.translate("&BSending you to &3Practice"));
                                    PriorityManager.removeFromVipPracticeQueue(pos1);
                                    plugin.setMainScoreboard(p);
                                    BungeeUtils.send(pos1, "Practice");
                                }
                            }
                        }, plugin.getConfig().getInt("queue.donator-delay") * 20);
                        //Sending them a message each Send
                        Runnable.scheduleSyncRepeatingTask(WHub.getINSTANCE(), new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!PriorityManager.vipPracticQueue.contains(p)) {
                                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                                }
                                if (urPos == 1) {
                                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                                }
                                if (urPos == 0) {
                                    Bukkit.getScheduler().cancelTask(this.getTaskId());
                                }
                                int urPos1 = urPos - 1;
                                if (PriorityManager.vipPracticQueue.lastIndexOf(p) == 0) {
                                    p.sendMessage(Color.translate(plugin.getConfig().getString("queue.message").replaceAll("<pos>", urPos + "#").replaceAll("<total>", PriorityManager.vipPracticQueue.size() + "#")));
                                }else {
                                    p.sendMessage(Color.translate(plugin.getConfig().getString("queue.message").replaceAll("<pos>", urPos + "#").replaceAll("<total>", PriorityManager.vipPracticQueue.size() + "#")));
                                }
                            }
                        },plugin.getConfig().getInt("queue.send-message-every") * 20, plugin.getConfig().getInt("queue.send-message-every") * 20);
                    }

                    //Putting the player in Queue!
                    int peopleinPracticeQueue = QueueManager.practiceQueue.size();
                    p.closeInventory();
                    QueueManager.inserttoPracticeQueue(p, peopleinPracticeQueue);
                    plugin.setInQueueScoreboard(p);
                    int urPos = QueueManager.practiceQueue.lastIndexOf(p) + 1;
                    //Getting the first place & sending them to Practice!
                    Bukkit.getScheduler().scheduleSyncDelayedTask(WHub.getINSTANCE(), new java.lang.Runnable() {
                        @Override
                        public void run() {
                            if (QueueManager.practiceQueue.get(0).getPlayer() != null) {
                                Player pos1 = QueueManager.practiceQueue.get(0);
                                pos1.sendMessage(Color.translate("&BSending you to &3Practice"));
                                QueueManager.removeFromPracticeQueue(pos1);
                                plugin.setMainScoreboard(p);
                                BungeeUtils.send(pos1, "Practice");
                            }
                        }
                    }, plugin.getConfig().getInt("queue.delay") * 20);

                    //Sending the pos of the player every 2 seconds
                    Bukkit.getScheduler().runTaskTimerAsynchronously(WHub.getINSTANCE(), new java.lang.Runnable() {
                        @Override
                        public void run() {
                            if (!QueueManager.practiceQueue.contains(p)) {
                                return;
                            }
                            if (urPos == 0 && QueueManager.practiceQueue.size() == 1) {
                                QueueManager.practiceQueue.remove(p);
                                return;
                            }

                            if (QueueManager.practiceQueue.lastIndexOf(p) == 0 && QueueManager.practiceQueue.size() == 0) {
                                QueueManager.practiceQueue.remove(p);
                                return;
                            }
                            int urPos1 = urPos - 1;
                            if (QueueManager.practiceQueue.lastIndexOf(p) == 0) {
                                p.sendMessage(Color.translate(plugin.getConfig().getString("queue.message").replaceAll("<pos>", String.valueOf(urPos) + "#").replaceAll("<total>", QueueManager.practiceQueue.size() + "#")));
                            }else {
                                p.sendMessage(Color.translate(plugin.getConfig().getString("queue.message").replaceAll("<pos>", urPos + "#").replaceAll("<total>", String.valueOf(QueueManager.practiceQueue.size()) + "#")));
                            }
                        }
                    }, 0, plugin.getConfig().getInt("queue.send-message-every") * 20);
                }

            }
        }
    }
    
    @EventHandler
    public void on(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equalsIgnoreCase(ChatColor.DARK_AQUA + "Server Selector")) {
            e.setCancelled(true);
        }else {
            return;
        }
    }
}
