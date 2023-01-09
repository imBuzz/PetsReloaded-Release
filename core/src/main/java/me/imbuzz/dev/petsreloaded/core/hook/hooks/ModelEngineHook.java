package me.imbuzz.dev.petsreloaded.core.hook.hooks;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import me.imbuzz.dev.petsreloaded.core.PetsReloaded;
import me.imbuzz.dev.petsreloaded.core.hook.ExternalPluginHook;
import me.imbuzz.dev.petsreloaded.core.objects.pets.ComponentEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.PetEntity;
import me.imbuzz.dev.petsreloaded.core.objects.pets.enums.PetStatus;
import org.bukkit.entity.Entity;

public class ModelEngineHook implements ExternalPluginHook {

    public void disguise(Entity pet, String modelName, PetStatus... statuses) {
        ActiveModel activeModel = ModelEngineAPI.api.getModelManager().createActiveModel(modelName);
        if (activeModel == null) {
            PetsReloaded.get().getLogger().severe("No ModelEngine model found with name: " + modelName);
            return;
        }

        ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(pet);
        if (modeledEntity == null) {
            PetsReloaded.get().getLogger().severe("Failed to create Modeled Entity");
            return;
        }

        modeledEntity.addActiveModel(activeModel);
        modeledEntity.detectPlayers();

        for (PetStatus status : statuses) {
            if (status == PetStatus.INVISIBLE) {
                modeledEntity.setInvisible(true);
            }
        }

    }

    public void undisguise(PetEntity pet) {
        clearModel(pet.getHeadEntity());

        for (ComponentEntity entity : pet.getEntities().values()) {
            clearModel(entity.getEntity());
        }
    }

    private void clearModel(Entity entity) {
        ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().getModeledEntity(entity.getUniqueId());
        if (modeledEntity != null) modeledEntity.clearModels();
    }


}
