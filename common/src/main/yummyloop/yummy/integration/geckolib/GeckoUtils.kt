package yummyloop.yummy.integration.geckolib

import me.shedaniel.architectury.annotations.ExpectPlatform
import me.shedaniel.architectury.registry.BlockProperties
import me.shedaniel.architectury.registry.RegistrySupplier
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.item.GeoArmorItem
import software.bernie.geckolib3.model.AnimatedGeoModel
import yummyloop.common.entity.AnimatableLivingEntity
import yummyloop.common.integration.gecko.GeckoGenericModel
import yummyloop.common.item.AnimatableBlockItem
import yummyloop.common.item.AnimatableItem
import yummyloop.yummy.ExampleMod
import yummyloop.yummy.ExampleMod.Register
import yummyloop.yummy.item.Ytem

object GeckoUtils {
    private const val MOD_ID = ExampleMod.MOD_ID

    data class Entry(
        val type: GeckoType,
        val obj: RegistrySupplier<*>,
        val model: AnimatedGeoModel<out IAnimatable>,
    )

    /**
     * List of gecko entries for late renderer registry on different platforms (forge/fabric)
     */
    val geckoEntryList: MutableList<Entry> = mutableListOf()

    object Items {

        /**
         * Registers the Item [itemID] and the gecko item renderer for it
         *
         * @param itemID Base item name
         * @param itemFunc Constructor of the item Class
         * @param model the item model
         */
        fun register(
            itemID: String,
            itemFunc: (Item.Settings) -> AnimatableItem,
            model: AnimatedGeoModel<out AnimatableItem> = GeckoGenericModel.item(MOD_ID, itemID),
        ): RegistrySupplier<Item> {
            val myItem = Register.item(itemID) { itemFunc.invoke(geckoItemSettings(Ytem.Settings(), model)) }
            geckoEntryList.add(Entry(GeckoType.Item, myItem, model))
            return myItem
        }

        fun registerBlockItem(
            blockItemId: String,
            blockSupplier: () -> Block = { Block(BlockProperties.of(Material.SOIL)) },
            blockItemSupplier: (Block, Item.Settings) -> AnimatableBlockItem = ::AnimatableBlockItem,
            itemModel: AnimatedGeoModel<out AnimatableBlockItem> = GeckoGenericModel.block(MOD_ID, blockItemId),
        ): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
            val myBlockItem =
                Register.blockItem(
                    blockItemId,
                    blockSupplier
                ) { block, settings ->
                    blockItemSupplier(block,
                        geckoItemSettings(settings, itemModel as AnimatedGeoModel<AnimatableItem>))
                }

            geckoEntryList.add(Entry(GeckoType.Item, myBlockItem.second, itemModel))
            return myBlockItem
        }

        fun registerBlockItem(
            blockItemId: String,
            blockSupplier: () -> Block = { Block(BlockProperties.of(Material.SOIL)) },
            model: AnimatedGeoModel<out AnimatableBlockItem> = GeckoGenericModel.block(MOD_ID, blockItemId),
        ): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
            return registerBlockItem(blockItemId, blockSupplier, ::AnimatableBlockItem, model)
        }


        /**
         * Registers a gecko Armor + their items with gecko renderers
         *
         * @param itemID Base item name
         * @param item0 Pair of ItemName to append + the item
         * @param item1 Pair of ItemName to append + the item
         * @param item2 Pair of ItemName to append + the item
         * @param item3 Pair of ItemName to append + the item
         * @param model the armor model
         */
        fun registerArmorWithItems(
            itemID: String,
            item0: Pair<String, (Item.Settings) -> AnimatableItem>,
            item1: Pair<String, (Item.Settings) -> AnimatableItem>? = null,
            item2: Pair<String, (Item.Settings) -> AnimatableItem>? = null,
            item3: Pair<String, (Item.Settings) -> AnimatableItem>? = null,
            model: AnimatedGeoModel<out AnimatableItem> = GeckoGenericModel.armor(MOD_ID, itemID),
        ): MutableList<RegistrySupplier<Item>> {
            val myList: MutableList<RegistrySupplier<Item>> = mutableListOf()
            myList.add(register(itemID + item0.first, item0.second, model))
            if (item1 != null)
                myList.add(register(itemID + item1.first, item1.second, model))
            if (item2 != null)
                myList.add(register(itemID + item2.first, item2.second, model))
            if (item3 != null)
                myList.add(register(itemID + item3.first, item3.second, model))

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
         * @param model the armor model
         */
        fun <T> registerArmor(
            itemID: String,
            item0: Pair<String, () -> T>,
            item1: Pair<String, () -> T>? = null,
            item2: Pair<String, () -> T>? = null,
            item3: Pair<String, () -> T>? = null,
            model: AnimatedGeoModel<T> = GeckoGenericModel.armor(MOD_ID, itemID),
        ): MutableList<RegistrySupplier<Item>> where T : GeoArmorItem, T : IAnimatable {
            val myList: MutableList<RegistrySupplier<Item>> = mutableListOf()
            myList.add(Register.item(itemID + item0.first, item0.second))
            if (item1 != null)
                myList.add(Register.item(itemID + item1.first, item1.second))
            if (item2 != null)
                myList.add(Register.item(itemID + item2.first, item2.second))
            if (item3 != null)
                myList.add(Register.item(itemID + item3.first, item3.second))

            geckoEntryList.add(Entry(GeckoType.Armor, myList.first(), model))
            return myList
        }
    }

    object Blocks {
        /**
         * Registers a gecko Block Entity renderer for the [blockEntityType] with [model]
         *
         * @param blockEntityType the blockEntityType registrySupplier
         * @param model the Block Entity Model
         */
        fun <T> register(
            blockEntityType: RegistrySupplier<out BlockEntityType<out BlockEntity>>,
            model: AnimatedGeoModel<T>,
        ) where T : BlockEntity, T : IAnimatable {
            geckoEntryList.add(Entry(GeckoType.Block, blockEntityType, model))
        }

        /**
         * Registers a gecko Block Entity renderer for the [blockEntityType] with [model]
         *
         * @param blockEntityType the blockEntityType registrySupplier
         */
        fun register(
            blockEntityType: RegistrySupplier<out BlockEntityType<out BlockEntity>>,
        ) {
            geckoEntryList.add(Entry(GeckoType.Block, blockEntityType,
                GeckoGenericModel.block(blockEntityType.id.namespace, blockEntityType.id.path)
            ))
        }
    }

    object Entities {
        /**
         * Registers a gecko Living Entity renderer for the [entityType] with [model]
         *
         * @param entityType the entityType registrySupplier
         * @param model the Living Entity Model
         */
        fun <T, A> register(
            entityType: RegistrySupplier<EntityType<T>>,
            model: AnimatedGeoModel<A> = GeckoGenericModel.entity(entityType.id.namespace, entityType.id.path),
        ) where T : Entity, A : AnimatableLivingEntity<T> {
            geckoEntryList.add(Entry(GeckoType.Entity, entityType, model))
        }
    }

    /**
     * Gets a platform dependent (forge) Gecko Supplier
     *
     * @param itemSettings the item settings for the item
     */
    @Suppress("UNUSED_PARAMETER")
    @JvmStatic
    @ExpectPlatform
    fun geckoItemSettings(
        itemSettings: Item.Settings,
        model: AnimatedGeoModel<out AnimatableItem>,
    ): Item.Settings = throw AssertionError()

    enum class GeckoType {
        Item,
        Armor,
        Block,
        Entity
    }
}