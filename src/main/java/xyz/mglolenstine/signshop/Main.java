package xyz.mglolenstine.signshop;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by LifEorDeatH on 17.7.2017.
 */
public class Main extends JavaPlugin{
    static Economy econ;

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new MyListener(), this);
        if(setupEconomy()){
            System.out.println(ChatColor.GREEN+"[SignShop] Vault has been successfully initialized");
        }else{
            System.out.println(ChatColor.RED+"[SignShop] Vault hasn't been successfully initialized");
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
