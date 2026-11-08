package io.github.ratherzenith.bmwillsync;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wayoftime.bloodmagic.util.Utils;
import wayoftime.bloodmagic.util.handler.event.GenericHandler;

/**
 * The Blood Magic Aura Gauge sends its chunk aura packet from
 * ItemDemonWillGauge#inventoryTick only when the player's tickCount is a
 * multiple of 50.
 *
 * This handler sends the exact same packet every server tick while the player
 * has an active aura viewer. The client-side HUD in Blood Magic
 * (ElementDemonAura) already redraws every frame and reads
 * ClientHandler.currentAura, so the displayed values become live without
 * replacing the HUD.
 */
@Mod.EventBusSubscriber(modid = BMWillsync.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = { Dist.CLIENT, Dist.DEDICATED_SERVER })
public final class ServerAuraSyncHandler {

    private ServerAuraSyncHandler() {
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        // The packet sender is server-only; never do this on the client.
        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        // Matches Blood Magic's own HUD condition, including active Curios
        // inventories. Without a viewer the HUD is not visible anyway.
        if (!Utils.canPlayerSeeDemonWill(player)) {
            return;
        }

        GenericHandler.sendPlayerDemonWillAura(player);
    }
}
