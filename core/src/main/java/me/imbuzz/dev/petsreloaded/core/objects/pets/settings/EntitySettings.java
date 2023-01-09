package me.imbuzz.dev.petsreloaded.core.objects.pets.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import me.imbuzz.dev.petsreloaded.api.managers.IPetSettings;
import me.imbuzz.dev.petsreloaded.core.utils.Strings;
import org.bukkit.ChatColor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.List;
import java.util.Map;

@Data
public class EntitySettings implements IPetSettings {

    private String tag = "", name = "", permission = "", petName = "", modelName = "";
    private double speed = 1.3, distanceInBlock = 10;
    private EntityType entityType = null;
    private boolean visibile = false, mountable = true;
    private List<String> description = Lists.newArrayList();

    private Map<String, ItemStack> symbols = Maps.newHashMap();
    private Map<String, ItemStack> items = Maps.newHashMap();
    private Map<String, StructureSettings> structureSettings = Maps.newHashMap();

    public String getPetName() {
        return ChatColor.stripColor(Strings.colorize(petName));
    }

    public static class StructureSettings {
        public String helmet = "", chestplate = "", legs = "", boots = "", handItem = "", displayName = "", tag = "", modelName = "";
        public boolean small = false, visibile = false, plate = false;
        public double xOffset = 0.0, yOffset = 0.0, zOffset = 0.0;

        public EulerAngle headPos = new EulerAngle(0, 0, 0), bodyPos = new EulerAngle(0, 0, 0), leftArmPos = new EulerAngle(0, 0, 0),
                rightArmPos = new EulerAngle(0, 0, 0), leftLegsPos = new EulerAngle(0, 0, 0), rightLegsPos = new EulerAngle(0, 0, 0);

        public void equip(ArmorStand armorStand, EntitySettings settings) {
            if (helmet != null && !helmet.isEmpty())
                armorStand.getEquipment()
                        .setHelmet(settings.getItems().get(helmet));

            if (chestplate != null && !chestplate.isEmpty())
                armorStand.getEquipment()
                        .setChestplate(settings.getItems().get(chestplate));

            if (legs != null && !legs.isEmpty())
                armorStand.getEquipment()
                        .setLeggings(settings.getItems().get(legs));

            if (boots != null && !boots.isEmpty())
                armorStand.getEquipment()
                        .setBoots(settings.getItems().get(boots));

            if (handItem != null && !handItem.isEmpty())
                armorStand.getEquipment()
                        .setItemInHand(settings.getItems().get(handItem));
        }
        public void pose(ArmorStand armorStand){
            armorStand.setHeadPose(headPos);
            armorStand.setBodyPose(bodyPos);
            armorStand.setLeftArmPose(leftArmPos);
            armorStand.setRightArmPose(rightArmPos);
            armorStand.setLeftLegPose(leftLegsPos);
            armorStand.setRightLegPose(rightLegsPos);
        }
    }


}
