package nl.skbotnl.rewindfixog

import net.minecraft.world.entity.EnumMoveType
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract
import net.minecraft.world.phys.Vec3D
import org.bukkit.util.Vector

class MoveHorseRunnable(private val horse: EntityHorseAbstract, private val vec: Vector) : Runnable {
    override fun run() {
        // move(EnumMoveType arg0, Vec3D arg1)
        horse.a(EnumMoveType.b, Vec3D(vec.x, vec.y, vec.z))
    }
}