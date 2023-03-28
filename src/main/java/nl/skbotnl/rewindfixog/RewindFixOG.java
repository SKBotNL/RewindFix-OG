package nl.skbotnl.rewindfixog;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.viaversion.viaversion.api.Via;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import net.minecraft.world.entity.vehicle.EntityBoat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftAbstractHorse;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftBoat;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftLlama;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RewindFixOG extends JavaPlugin implements Listener {

    private RewindFixOG plugin;

    @Override
    public void onEnable() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        plugin = this;

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getVehicle() == null) {
                        return;
                    }

                    if (Via.getAPI().getPlayerVersion(player) > 107) {
                        return;
                    }

                    if (player.getVehicle() instanceof CraftLlama) {
                        return;
                    }

                    if (player.getVehicle() instanceof CraftAbstractHorse abstractHorse) {
                        if (abstractHorse.getInventory().getSaddle() == null) {
                            return;
                        }

                        Vector downVec = new Vector(0, -1, 0);
                        EntityHorseAbstract nmsHorse = abstractHorse.getHandle();
                        Bukkit.getScheduler().runTask(plugin, new MoveHorseRunnable(nmsHorse, downVec));
                    }
                }
            }
        }.runTaskTimer(this, 0L, 1L);

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

                if (forward == 0 && sideways == 0) {
                    return;
                }

                if (event.getPlayer().getVehicle() == null) {
                    return;
                }

                if (player.getVehicle() instanceof CraftLlama) {
                    return;
                }

                if (event.getPlayer().getVehicle() instanceof CraftBoat craftBoat) {

                    EntityBoat nmsBoat = craftBoat.getHandle();

                    if (!(Helper.getBoatTimeout(player.getUniqueId()) == 0)) {
                        return;
                    }

                    Location loc = player.getLocation();
                    Vector vec = loc.getDirection();
                    vec.setY(0);

                    Bukkit.getScheduler().runTask(plugin, new MoveBoatRunnable(nmsBoat, vec));
                }
                else if (event.getPlayer().getVehicle() instanceof CraftAbstractHorse craftHorse) {
                    if (craftHorse.getInventory().getSaddle() == null) {
                        return;
                    }
                    EntityHorseAbstract nmsHorse = craftHorse.getHandle();
                    Location loc = player.getLocation();
                    Vector vec = loc.getDirection();
                    vec.setY(0);
                    vec.multiply(0.75);
                    Bukkit.getScheduler().runTask(plugin, new MoveHorseRunnable(nmsHorse, vec));
                }
            }
        });
    }
}
