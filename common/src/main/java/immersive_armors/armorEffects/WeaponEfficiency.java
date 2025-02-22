package immersive_armors.armorEffects;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WeaponEfficiency extends ArmorEffect {
    private final float damage;
    private final Class weapon;

    public WeaponEfficiency(float damage, Class items) {
        this.damage = damage;
        weapon = items;
    }

    @Override
    public float applyArmorToDamage(LivingEntity entity, DamageSource source, float amount, ItemStack armor) {
        return amount;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        TranslatableText weaponText = new TranslatableText("armorEffect.weaponEfficiency." + weapon.getSimpleName());
        tooltip.add(new TranslatableText("armorEffect.weaponEfficiency", (int)(damage * 100), weaponText).formatted(Formatting.GOLD));
    }

    @Override
    public float applyArmorToAttack(LivingEntity target, DamageSource source, float amount, ItemStack armor) {
        if (!source.isProjectile() && source.getAttacker() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity)source.getAttacker();
            if (isPrimaryArmor(armor, attacker)) {
                boolean hasAxe = Stream.of(attacker.getEquippedStack(EquipmentSlot.MAINHAND), attacker.getEquippedStack(EquipmentSlot.OFFHAND))
                        .filter(Objects::nonNull)
                        .anyMatch(v -> weapon.isInstance(v.getItem()));
                if (hasAxe) {
                    amount *= (1.0f + getSetCount(armor, attacker) * damage);
                }
            }
        }
        return amount;
    }
}
