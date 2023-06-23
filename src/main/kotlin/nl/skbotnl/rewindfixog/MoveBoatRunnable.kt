package nl.skbotnl.rewindfixog

import net.minecraft.world.entity.EnumMoveType
import net.minecraft.world.entity.vehicle.EntityBoat
import net.minecraft.world.phys.Vec3D
import org.bukkit.util.Vector

class MoveBoatRunnable(private val boat: EntityBoat, private val vec: Vector) : Runnable {
    override fun run() {
        // move(EnumMoveType arg0, Vec3D arg1)
        boat.a(EnumMoveType.b, Vec3D(vec.x, vec.y, vec.z))
    }
}