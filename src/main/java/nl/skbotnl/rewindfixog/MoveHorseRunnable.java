package nl.skbotnl.rewindfixog;

import net.minecraft.world.entity.EnumMoveType;
import net.minecraft.world.entity.animal.horse.EntityHorseAbstract;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.util.Vector;

public class MoveHorseRunnable implements Runnable {
    private final EntityHorseAbstract horse;
    private final Vector vec;

    public MoveHorseRunnable(EntityHorseAbstract horse, Vector vec) {
        this.horse = horse;
        this.vec = vec;
    }

    @Override
    public void run() {
        // move(EnumMoveType arg0, Vec3D arg1)
        horse.a(EnumMoveType.b, new Vec3D(vec.getX(), vec.getY(), vec.getZ()));
    }
}