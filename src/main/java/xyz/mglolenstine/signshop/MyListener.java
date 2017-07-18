package xyz.mglolenstine.signshop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
    private Economy econ;

    @EventHandler
    void onSignClick(PlayerInteractEvent e) {
        econ = Main.econ;
        if (e.getClickedBlock().getType() == Material.WALL_SIGN) {
            Sign s = (Sign) e.getClickedBlock().getState();
            String[] text = s.getLines();
            if (text[0].equals(ChatColor.BLUE + "[SignShop]")) {
                Material mat = Material.getMaterial(text[1]);
                String[] prices = text[2].split(":");
                int[] price = {Integer.parseInt(prices[0]), Integer.parseInt(prices[1])};
                for(int i : price) {
                    if (i < 0){
                        e.getPlayer().sendMessage("[SignShop] This shop isn't configured correctly, please contact administrators.");
                        return;
                    }
                }
                int amount = Integer.parseInt(text[3]);
                Player op = e.getPlayer();
                econ.createPlayerAccount(op);
                if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                    //Buy action
                    if (econ.getBalance(op) >= price[0]) {
                        e.getPlayer().getInventory().addItem(new ItemStack(mat, amount));
                        econ.withdrawPlayer(op, price[0]);
                        e.getPlayer().sendMessage("[SignShop] Item has been bought.");
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED + "[SignShop] You don't have enough money.");
                    }
                } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    //Sell action
                    if (contains(e.getPlayer(), mat, amount)) {
                        remove(e.getPlayer(), new ItemStack(mat, amount));
                        econ.depositPlayer(op, price[1]);
                        e.getPlayer().sendMessage("[SignShop] Item has been sold.");
                    } else {
                        e.getPlayer().sendMessage(ChatColor.RED + "[SignShop] You don't have enough items to sell.");
                    }
                }
            }
        }
    }

    @EventHandler
    void onSignCreate(SignChangeEvent e) {
        Player p = e.getPlayer();
        String[] text = e.getLines();
        if (text[0].equals("[SignShop]")) {
            if (p.hasPermission("signshop.make")) {
                e.setLine(0, ChatColor.BLUE + "[SignShop]");
                e.getBlock().getState().update();
            } else {
                e.getPlayer().sendMessage("[SignShop] Not enough permissions to create sign shop.");
            }
        }
    }
    private boolean contains(Player p, Material mat, int amount){
        int count = 0;
        for(int i = 0; i < p.getInventory().getSize(); i++){
            if(p.getInventory().getItem(i) != null) {
                if (p.getInventory().getItem(i).getType() == mat) {
                    count += p.getInventory().getItem(i).getAmount();
                    if (count >= amount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void remove(Player p, ItemStack is){
        int amount = is.getAmount();
        int count = 0;
        Material mat = is.getType();
        for(int i = 0; i < p.getInventory().getSize() && count < amount; i++){
            ItemStack tmp = p.getInventory().getItem(i);
            if(tmp != null){
                if(tmp.getType() == mat){
                    if(tmp.getAmount() >= amount-count){
                        tmp.setAmount(tmp.getAmount()-(amount-count));
                        p.getInventory().setItem(i, tmp);
                        return;
                    }else if(tmp.getAmount() < amount-count){
                        count += tmp.getAmount();
                        p.getInventory().getItem(i).setAmount(0);
                    }
                }
            }
        }
    }
}
