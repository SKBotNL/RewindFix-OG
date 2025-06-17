package nl.skbotnl.rewindfixog

import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.vehicle.Boat
import net.minecraft.world.phys.Vec3
import org.bukkit.util.Vector

class MoveBoatRunnable(private val boat: Boat, private val vec: Vector) : Runnable {
    override fun run() {
        boat.move(MoverType.PLAYER, Vec3(vec.x, vec.y, vec.z))
    }
}
