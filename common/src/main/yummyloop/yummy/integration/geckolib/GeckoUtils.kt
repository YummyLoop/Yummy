package yummyloop.yummy.integration.geckolib

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import yummyloop.yummy.ExampleMod
import yummyloop.yummy.ExampleMod.Register
import java.util.function.Supplier
import kotlin.reflect.KFunction1

object GeckoUtils {
    private const val MOD_ID = ExampleMod.MOD_ID

    data class Entry<T>(
        val type: GeckoType,
        val obj: RegistrySupplier<*>,
        val model: AnimatedGeoModel<T>,
    ) where T : IAnimatable

    /**
     * List of gecko items for late render registry on different platforms (forge/fabric)
     */
    val geckoEntryList: MutableList<Entry<*>> = mutableListOf()

    object Items {

        /**
         * Registers a Gecko Item
         *
         * @param itemID Base item name
         * @param itemFunc Constructor of the item Class
         * @param itemSettings Pair of ItemName to append + the item settings for said item
         * @param modID Mod id
         */
        fun <I> register(
            itemID: String,
            itemFunc: KFunction1<Item.Settings, I>,
            itemSettings: Item.Settings,
            modID: String = MOD_ID,
            model: AnimatedGeoModel<I> = GeckoGenericModel.item(modID, itemID),
        ): RegistrySupplier<Item> where I : Item, I : IAnimatable {
            val myItem = Register.item(itemID, geckoItemSupplier(itemFunc, itemSettings, model))
            geckoEntryList.add(Entry(GeckoType.Item, myItem, model))
            return myItem
        }


        /**
         * Registers a gecko Armor + their items as Gecko Items
         *
         * @param itemID Base item name
         * @param itemFunc Constructor of the item Class
         * @param itemSettings0 Pair of ItemName to append + the item settings for said item
         * @param itemSettings1 Pair of ItemName to append + the item settings for said item
         * @param itemSettings2 Pair of ItemName to append + the item settings for said item
         * @param itemSettings3 Pair of ItemName to append + the item settings for said item
         * @param modelLocation Model location
         * @param textureLocation Texture location
         * @param animationFileLocation Animation location
         * @param modID Mod id
         */
        fun <I> registerArmor(
            itemID: String,
            itemFunc: KFunction1<Item.Settings, I>,
            itemSettings0: Pair<String, Item.Settings>,
            itemSettings1: Pair<String, Item.Settings>? = null,
            itemSettings2: Pair<String, Item.Settings>? = null,
            itemSettings3: Pair<String, Item.Settings>? = null,
            modID: String = MOD_ID,
            model: AnimatedGeoModel<I> = GeckoGenericModel.armor(modID, itemID),
        ): MutableList<RegistrySupplier<Item>> where I : Item, I : IAnimatable {
            val myList: MutableList<RegistrySupplier<Item>> = mutableListOf()
            myList.add(register(itemID + itemSettings0.first,
                itemFunc,
                itemSettings0.second,
                modID, model))
            if (itemSettings1 != null) myList.add(register(itemID + itemSettings1.first,
                itemFunc,
                itemSettings1.second,
                modID, model))
            if (itemSettings2 != null) myList.add(register(itemID + itemSettings2.first,
                itemFunc,
                itemSettings2.second,
                modID, model))
            if (itemSettings3 != null) myList.add(register(itemID + itemSettings3.first,
                itemFunc,
                itemSettings3.second,
                modID, model))

            geckoEntryList.add(Entry(GeckoType.Armor, myList.first(), model))
            return myList
        }

        /**
         * Registers a gecko Armor (their items are registered as vanilla items)
         *
         * @param itemID Base item name
         * @param item0 Pair of ItemName to append + the item supplier for said item
         * @param item1 Pair of ItemName to append + the item supplier for said item
         * @param item2 Pair of ItemName to append + the item supplier for said item
         * @param item3 Pair of ItemName to append + the item supplier for said item
         * @param modelLocation Model location
         * @param textureLocation Texture location
         * @param animationFileLocation Animation location
         * @param modID Mod id
         */
        fun <T> registerArmor(
            itemID: String,
            item0: Pair<String, Supplier<T>>,
            item1: Pair<String, Supplier<T>>? = null,
            item2: Pair<String, Supplier<T>>? = null,
            item3: Pair<String, Supplier<T>>? = null,
            modID: String = MOD_ID,
            model: AnimatedGeoModel<T> = GeckoGenericModel.armor(modID, itemID),
        ): MutableList<RegistrySupplier<Item>> where T : GeoArmorItem, T : IAnimatable {
            val myList: MutableList<RegistrySupplier<Item>> = mutableListOf()
            myList.add(Register.item(itemID + item0.first, item0.second))
            if (item1 != null) myList.add(Register.item(itemID + item1.first, item1.second))
            if (item2 != null) myList.add(Register.item(itemID + item2.first, item2.second))
            if (item3 != null) myList.add(Register.item(itemID + item3.first, item3.second))
            geckoEntryList.add(Entry(GeckoType.Armor, myList.first(), model))
            return myList
        }
    }

    object Blocks {
        abstract class AnimatableBlockEntity(type: BlockEntityType<*>) : BlockEntity(type), IAnimatable

        fun <T, A> register(
            blockEntityType: RegistrySupplier<BlockEntityType<T>>,
            model: AnimatedGeoModel<A> = GeckoGenericModel.block(blockEntityType.id.namespace, blockEntityType.id.path),
        ) where T : BlockEntity, A : AnimatableBlockEntity {
            geckoEntryList.add(Entry(GeckoType.Block, blockEntityType, model))
        }
    }

    abstract class AnimatableEntity(type: EntityType<*>, world: World?) : Entity(type, world), IAnimatable
    object Entities {
        fun <T, A> register(
            entityType: RegistrySupplier<EntityType<T>>,
            model: AnimatedGeoModel<A> = GeckoGenericModel.entity(entityType.id.namespace, entityType.id.path),
        ) where T : Entity, A : AnimatableEntity {
            geckoEntryList.add(Entry(GeckoType.Entity, entityType, model))
        }
    }

    /**
     * Gets a platform dependent (forge) Gecko Supplier
     *
     * @param itemFunc Constructor of the item Class
     * @param itemSettings Pair of ItemName to append + the item settings for said item
     * @param modelLocation Model location
     * @param textureLocation Texture location
     * @param animationFileLocation Animation location
     * @param modID Mod id
     */
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    @ExpectPlatform
    fun <I> geckoItemSupplier(
        itemFunc: KFunction1<Item.Settings, I>,
        itemSettings: Item.Settings,
        model: AnimatedGeoModel<I>,
    ): Supplier<out I> where I : Item, I : IAnimatable = throw AssertionError()

    enum class GeckoType {
        Item,
        Armor,
        Block,
        Entity
    }
}