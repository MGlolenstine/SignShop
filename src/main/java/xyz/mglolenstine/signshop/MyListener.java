package xyz.mglolenstine.signshop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by LifEorDeatH on 17.7.2017.
 */
class MyListener implements Listener {
    private Economy econ = Main.econ;

    @EventHandler
    void onSignClick(PlayerInteractEvent e) {
        if (e.getClickedBlock().getType() == Material.WALL_SIGN) {
            Sign s = (Sign) e.getClickedBlock();
            String[] text = s.getLines();
            if (text[0].equals(ChatColor.BLUE + "[SignShop]")) {
                Material mat = Material.getMaterial(text[1]);
                String[] prices = text[2].split(":");
                int[] price = {Integer.parseInt(prices[0]), Integer.parseInt(prices[1])};
                int amount = Integer.parseInt(text[3]);
                OfflinePlayer op = Bukkit.getOfflinePlayer(e.getPlayer().getUniqueId());
                if (!econ.hasAccount(op)) {
                    econ.createPlayerAccount(op);
                }
                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    //Buy action
                    if (econ.getBalance(op) >= price[0]) {
                        e.getPlayer().getInventory().addItem(new ItemStack(mat, amount));
                        econ.withdrawPlayer(op, price[0]);
                        e.getPlayer().sendMessage("Item has been bought.");
                    }else{
                        e.getPlayer().sendMessage(ChatColor.RED+"[SignShop] You don't have enough money.");
                    }
                } else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    //Sell action
                    if (e.getPlayer().getInventory().contains(mat, amount)) {
                        e.getPlayer().getInventory().remove(new ItemStack(mat, amount));
                        econ.depositPlayer(op, price[1]);
                        e.getPlayer().sendMessage("Item has been sold.");
                    }else{
                        e.getPlayer().sendMessage(ChatColor.RED+"[SignShop] You don't have enough items to sell.");
                    }
                }
            }
        }
    }

    @EventHandler
    void onSignCreate(SignChangeEvent e) {
        Player p = e.getPlayer();
        String[] text = e.getLines();
        if (text[0].equals(ChatColor.BLUE + "[SignShop]")) {
            if (p.hasPermission("signshop.make")) {
                e.setLine(0, ChatColor.BLUE + "[SignShop]");
                ((Sign) e.getBlock()).update();
            }
        }
    }
}
