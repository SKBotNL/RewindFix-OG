package nl.skbotnl.rewindfixog

import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.vehicle.VehicleDestroyEvent

class Events : Listener {
    @EventHandler
    fun onVehicleDestroy(event: VehicleDestroyEvent) {
        if (event.vehicle is Boat && event.attacker == null) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        if (RewindFixOG.viaApi.getPlayerVersion(player.uniqueId) > 107) {
            return
        }
        if (player.isSprinting && player.world.getBlockAt(player.location.add(0.0, 1.5, 0.0)).type == Material.WATER) {
            player.velocity = player.location.direction.multiply(0.26)
        }
    }
}
