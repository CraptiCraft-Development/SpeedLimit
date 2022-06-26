package me.loving11ish.speedlimit.events;

import me.loving11ish.speedlimit.SpeedLimit;
import me.loving11ish.speedlimit.utils.ColorUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ElytraTPSFlightEvent implements Listener {

    private FileConfiguration configFile = SpeedLimit.getPlugin().getConfig();
    private FileConfiguration messagesFile = SpeedLimit.getPlugin().messagesDataManager.getMessagesConfig();
    private String PREFIX = messagesFile.getString("plugin-prefix");
    private static final String PREFIX_PLACEHOLDER = "%PREFIX%";

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTPSElytraFlightCheck(PlayerMoveEvent event){
        Player player = event.getPlayer();
        double x = event.getFrom().getX();
        double z = event.getFrom().getZ();
        float yaw = event.getFrom().getYaw();
        float pitch = event.getFrom().getPitch();
        if (configFile.getBoolean("tps.dynamic-elytra-check.enabled")){
            if (SpeedLimit.getServerTPS() <= configFile.getDouble("tps.dynamic-elytra-check.trigger-value")){
                double yTop = event.getFrom().getY();
                Location location = new Location(player.getWorld(), x, yTop, z, yaw, pitch);
                yTop = location.getWorld().getHighestBlockYAt(location);
                location.setY(yTop);
                player.setGliding(false);
                player.teleport(location);
                if (configFile.getBoolean("tps.dynamic-elytra-check.send-warning-message")){
                    player.sendMessage(ColorUtils.translateColorCodes(
                            messagesFile.getString("TPS-elytras-disabled-warning").replace(PREFIX_PLACEHOLDER, PREFIX)));
                }
            }
        }
    }
}