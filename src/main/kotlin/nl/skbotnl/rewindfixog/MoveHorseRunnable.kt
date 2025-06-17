package nl.skbotnl.rewindfixog

import net.minecraft.world.entity.MoverType
import net.minecraft.world.entity.animal.horse.AbstractHorse
import net.minecraft.world.phys.Vec3
import org.bukkit.util.Vector

class MoveHorseRunnable(private val horse: AbstractHorse, private val vec: Vector) : Runnable {
    override fun run() {
        horse.move(MoverType.PLAYER, Vec3(vec.x, vec.y, vec.z))
    }
}
