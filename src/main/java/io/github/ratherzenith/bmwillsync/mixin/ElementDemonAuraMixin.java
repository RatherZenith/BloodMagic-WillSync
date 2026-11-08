package io.github.ratherzenith.bmwillsync.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wayoftime.bloodmagic.client.hud.element.ElementDemonAura;

/**
 * Blood Magic's Aura Gauge HUD normally shows the numeric values only while
 * Shift is held. This mixin inverts only that decision:
 *
 *   Shift held     -> hide numbers
 *   Shift not held -> show numbers
 *
 * The bars themselves and all other HUD behavior are untouched.
 */
@Mixin(ElementDemonAura.class)
public class ElementDemonAuraMixin {

    @Redirect(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;isShiftKeyDown()Z"))
    private boolean bmwillsync$invertShiftNumberDisplay(Player player) {
        return !player.isShiftKeyDown();
    }
}
