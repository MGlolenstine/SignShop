package xyz.mglolenstine.signshop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by LifEorDeatH on 17.7.2017.
 */
public class MyListener implements Listener {
    @EventHandler
    void onSignClick(PlayerInteractEvent e){
        if(e.getClickedBlock().getType() == Material.WALL_SIGN){
            Sign s = (Sign) e.getClickedBlock();
            String[] text = s.getLines();
            if(text[0].equals(ChatColor.BLUE+"[SignShop]")){

            }
        }
    }
}
