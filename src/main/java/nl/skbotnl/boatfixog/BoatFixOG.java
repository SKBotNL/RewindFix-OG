package nl.skbotnl.boatfixog;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.viaversion.viaversion.api.Via;
import net.minecraft.world.entity.vehicle.EntityBoat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftBoat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class BoatFixOG extends JavaPlugin {

    @Override
    public void onEnable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();
                if (Via.getAPI().getPlayerVersion(player) > 107) {
                    return;
                }

                PacketContainer packet = event.getPacket();

                float sideways = packet.getFloat().read(0);
                float forward = packet.getFloat().read(1);

                if (!(event.getPlayer().getVehicle() instanceof CraftBoat)) {
                    return;
                }

                if (forward == 0 && sideways == 0) {
                    return;
                }

                CraftBoat craftBoat = (CraftBoat) event.getPlayer().getVehicle();
                if (craftBoat == null) {
                    return;
                }

                EntityBoat nmsBoat = craftBoat.getHandle();

                if (!(Helper.getBoatTimeout(player.getUniqueId()) == 0)) {
                    return;
                }

                Location loc = event.getPlayer().getLocation();
                Vector vec = loc.getDirection();
                vec.setY(0);

                Bukkit.getScheduler().runTask(plugin, new MoveBoatRunnable(nmsBoat, vec));
            }
        });
    }
}
