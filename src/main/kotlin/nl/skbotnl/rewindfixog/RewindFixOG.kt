package nl.skbotnl.rewindfixog

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.viaversion.viaversion.api.Via
import com.viaversion.viaversion.api.ViaAPI
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraft.world.entity.vehicle.Boat
import org.bukkit.Bukkit
import org.bukkit.attribute.Attribute
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftAbstractHorse
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftBoat
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftLlama
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class RewindFixOG : JavaPlugin() {
    companion object {
        lateinit var plugin: RewindFixOG
        lateinit var viaApi: ViaAPI<Any>
    }

    override fun onEnable() {
        val protocolManager = ProtocolLibrary.getProtocolManager()
        plugin = this
        viaApi = Via.getAPI()
        object : BukkitRunnable() {
                override fun run() {
                    for (player in Bukkit.getOnlinePlayers()) {
                        if (player.vehicle == null) {
                            return
                        }
                        if (viaApi.getPlayerVersion(player.uniqueId) > 107) {
                            return
                        }
                        if (player.vehicle is CraftAbstractHorse && player.vehicle !is CraftLlama) {
                            val abstractHorse = player.vehicle as CraftAbstractHorse
                            if (abstractHorse.inventory.saddle == null) {
                                return
                            }
                            val downVec = Vector(0, -1, 0)
                            val nmsHorse = abstractHorse.handle
                            Bukkit.getScheduler().runTask(plugin, MoveHorseRunnable(nmsHorse, downVec))
                        }
                        if (player.vehicle is CraftBoat) {
                            if ((player.vehicle as CraftBoat).isInWater) {
                                return
                            }
                            val craftBoat = player.vehicle as CraftBoat
                            val downVec = Vector(0.0, -0.5, 0.0)
                            val nmsBoat = craftBoat.handle
                            Bukkit.getScheduler().runTask(plugin, MoveBoatRunnable(nmsBoat, downVec))
                        }
                    }
                }
            }
            .runTaskTimer(this, 0L, 1L)
        protocolManager.addPacketListener(
            object : PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.STEER_VEHICLE) {
                override fun onPacketReceiving(event: PacketEvent) {
                    val player = event.player
                    val api = Via.getAPI()
                    if (api.getPlayerVersion(player.uniqueId) > 107) {
                        return
                    }
                    val packet = event.packet
                    val sideways = packet.float.read(0)
                    val forward = packet.float.read(1)
                    if (forward == 0f && sideways == 0f) {
                        return
                    }
                    if (event.player.vehicle == null) {
                        return
                    }
                    if (player.vehicle is CraftLlama) {
                        return
                    }
                    if (event.player.vehicle is CraftBoat) {
                        val craftBoat = event.player.vehicle as CraftBoat
                        val nmsBoat: Boat = craftBoat.handle
                        if (Helper.getBoatTimeout(player.uniqueId) != 0L) {
                            return
                        }
                        val loc = player.location
                        val vec = loc.direction
                        vec.setY(0)
                        Bukkit.getScheduler().runTask(plugin, MoveBoatRunnable(nmsBoat, vec))
                    } else if (event.player.vehicle is CraftAbstractHorse) {
                        val craftHorse = event.player.vehicle as CraftAbstractHorse
                        if (craftHorse.inventory.saddle == null) {
                            return
                        }
                        val speed = craftHorse.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.value ?: return
                        val nmsHorse: AbstractHorse = craftHorse.handle
                        val loc = player.location
                        val vec = loc.direction
                        vec.setY(0)
                        vec.multiply(3 * speed)
                        Bukkit.getScheduler().runTask(plugin, MoveHorseRunnable(nmsHorse, vec))
                    }
                }
            }
        )
        this.server.pluginManager.registerEvents(Events(), this)
    }
}
