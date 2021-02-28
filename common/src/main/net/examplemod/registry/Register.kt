package net.examplemod.registry

import me.shedaniel.architectury.platform.Platform
import me.shedaniel.architectury.registry.BlockProperties
import me.shedaniel.architectury.registry.DeferredRegister
import me.shedaniel.architectury.registry.MenuRegistry
import me.shedaniel.architectury.registry.RegistrySupplier
import net.examplemod.ExampleMod
import net.examplemod.ModContent
import net.examplemod.items.Ytem
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text
import net.minecraft.util.registry.Registry
import java.util.function.Supplier
import kotlin.reflect.KFunction3

/** Contains the declared registers, and the functions to register new content */
object Register {
    private const val modId = ExampleMod.MOD_ID

    /** Final registry of the content */
    internal fun register() {
        ModContent
        entityTypeRegister.register()
        entityAttributeRegister.register()
        blockRegister.register()
        blockEntityTypeRegister.register()
        itemRegister.register()
        EntityAttributeLink.register()
        screenHandlerTypeRegister.register()
    }

    /** Block register */
    private val blockRegister: DeferredRegister<Block> =
        DeferredRegister.create(modId, Registry.BLOCK_KEY)

    /** Block Entity Type register */
    private val blockEntityTypeRegister: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(modId, Registry.BLOCK_ENTITY_TYPE_KEY)

    /** Entity Type register */
    private val entityTypeRegister: DeferredRegister<EntityType<*>> =
        DeferredRegister.create(modId, Registry.ENTITY_TYPE_KEY)

    /** Entity Attribute register */
    private val entityAttributeRegister: DeferredRegister<EntityAttribute> =
        DeferredRegister.create(modId, Registry.ATTRIBUTE_KEY)

    /** Item register */
    private val itemRegister: DeferredRegister<Item> =
        DeferredRegister.create(modId, Registry.ITEM_KEY)

    /** Menu register */
    private val screenHandlerTypeRegister: DeferredRegister<ScreenHandlerType<*>> =
        DeferredRegister.create(modId, Registry.MENU_KEY)

    // We can use this if we don't want to use DeferredRegister
    // val REGISTRIES by lazyOf(Registries.get(ExampleMod.MOD_ID))
    // var lazyItems = REGISTRIES.get(Registry.ITEM_KEY)
    // var lazyItem = lazyItems.registerSupplied(Identifier(ExampleMod.MOD_ID, "example_lazy_item"), ::Ytem)}

    /**
     * Registers a new item
     *
     * @param itemId Id of the item
     * @param itemSupplier The supplier used to create the item
     * @return A RegistrySupplier for the item
     */
    fun item(
        itemId: String,
        itemSupplier: Supplier<out Item> = Supplier { Ytem() },
    ): RegistrySupplier<Item> = itemRegister.register(itemId, itemSupplier)

    /**
     * Registers a new block
     *
     * @param blockId Id of the block
     * @param blockSupplier The supplier used to create the block
     * @return A RegistrySupplier for the block
     */
    fun block(
        blockId: String,
        blockSupplier: Supplier<out Block> = Supplier { Block(BlockProperties.of(Material.SOIL)) },
    ): RegistrySupplier<Block> = blockRegister.register(blockId, blockSupplier)

    /**
     * Registers a new block and a corresponding item
     *
     * @param blockItemId Id of the block and item
     * @param blockSupplier The supplier used to create the block
     * @param itemSettings The item settings used to create the item
     * @return A Pair of RegistrySuppliers for the block and item
     */
    fun blockItem(
        blockItemId: String,
        blockSupplier: Supplier<out Block> = Supplier { Block(BlockProperties.of(Material.SOIL)) },
        itemSettings: Item.Settings = Ytem.Settings(),
    ): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
        val block = this.block(blockItemId, blockSupplier)
        val item = this.item(blockItemId) { BlockItem(block.get(), itemSettings) }
        return Pair(block, item)
    }

    /**
     * Registers/Associates a blockEntityType with multiple blocks
     *
     * @param blockEntityTypeId Id of the block entity type
     * @param blockEntityTypeSupplier The supplier used to create the block entity
     * @param blocks The blocks to be associated with the block entity type
     * @return A RegistrySupplier for the block entity type
     */
    fun <T> blockEntityType(
        blockEntityTypeId: String,
        blockEntityTypeSupplier: Supplier<T>,
        vararg blocks: RegistrySupplier<Block>,
    ): RegistrySupplier<BlockEntityType<T>> where T : BlockEntity {
        return blockEntityTypeRegister.register(blockEntityTypeId) {
            BlockEntityType.Builder.create(
                blockEntityTypeSupplier,
                *blocks.map { it.get() }.toTypedArray()
            ).build(null)
        }
    }

    /**
     * Registers an EntityType
     *
     * @param entityTypeId Id of the block entity type
     * @param entityTypeBuilder The builder used to create the entity
     * @return A RegistrySupplier for the entity type
     */
    fun <T> entityType(
        entityTypeId: String,
        entityTypeBuilder: Supplier<EntityType.Builder<T>>,
    ): RegistrySupplier<EntityType<T>> where T : Entity {
        return entityTypeRegister.register(entityTypeId) { entityTypeBuilder.get().build(entityTypeId) }
    }

    /**
     * Registers an EntityAttribute
     *
     * @param entityAttributeId Id of the Attribute
     * @param entityAttribute The Attribute
     * @return A RegistrySupplier for the entity type
     */
    fun <T> entityAttribute(
        entityAttributeId: String,
        entityAttribute: Supplier<T>,
    ): RegistrySupplier<T> where T : EntityAttribute {
        return entityAttributeRegister.register(entityAttributeId, entityAttribute)
    }

    /**
     * Links an EntityType with the EntityAttributes
     *
     * @param entityType Entity Type
     * @param entityAttributeBuilder The Attribute builder
     */
    fun entityAttributeLink(
        entityType: RegistrySupplier<out EntityType<out LivingEntity>>,
        entityAttributeBuilder: Supplier<DefaultAttributeContainer.Builder>,
    ) {
        EntityAttributeLink.register(entityType, entityAttributeBuilder)
    }

    /**
     * Registers a ScreenHandlerType
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param factory The factory of the ScreenHandlerType
     */
    fun <T> screenHandlerType(
        screenHandlerTypeId: String,
        factory: Supplier<ScreenHandlerType<T>>,
    ): RegistrySupplier<ScreenHandlerType<T>> where T : ScreenHandler {
        return screenHandlerTypeRegister.register(screenHandlerTypeId, factory)
    }

    /**
     * Registers a Simple ScreenHandlerType
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param factory The factory of the ScreenHandlerType
     */
    fun <T> screenHandlerTypeSimple(
        screenHandlerTypeId: String,
        factory: MenuRegistry.SimpleMenuTypeFactory<T>,
    ): RegistrySupplier<ScreenHandlerType<T>> where T : ScreenHandler {
        return screenHandlerType(screenHandlerTypeId) { MenuRegistry.of(factory) }
    }

    /**
     * Registers an Extended ScreenHandlerType
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param factory The factory of the ScreenHandlerType
     */
    fun <T> screenHandlerTypeExtended(
        screenHandlerTypeId: String,
        factory: MenuRegistry.ExtendedMenuTypeFactory<T>,
    ): RegistrySupplier<ScreenHandlerType<T>> where T : ScreenHandler {
        return screenHandlerType(screenHandlerTypeId) { MenuRegistry.ofExtended(factory) }
    }

    object Client {
        private val isClient: Boolean by lazy { Platform.getEnv() == EnvType.CLIENT }
        private val lateCallList: MutableList<Supplier<Any>> = mutableListOf()

        @Environment(EnvType.CLIENT)
        fun register() {
            lateCallList.forEach { it.get() }
            lateCallList.clear()
        }

        fun <H, S> screen(
            screenHandlerType: RegistrySupplier<ScreenHandlerType<H>>,
            screenFactory: Supplier<*>,
        ) where H : ScreenHandler, S : Screen, S : ScreenHandlerProvider<H> {
            if (isClient) lateCallList.add(Supplier {
                try {//it works with Supplier<KFunction3<H, PlayerInventory, Text, S>>
                    MenuRegistry.registerScreenFactory(
                        screenHandlerType.get(),
                        @Suppress("UNCHECKED_CAST") (screenFactory as Supplier<KFunction3<H, PlayerInventory, Text, S>>).get())
                } catch (e: Exception) {
                    try {//but the factory should be ? Supplier<MenuRegistry.ScreenFactory<H, S>>
                        MenuRegistry.registerScreenFactory(
                            screenHandlerType.get(),
                            @Suppress("UNCHECKED_CAST") (screenFactory as Supplier<MenuRegistry.ScreenFactory<H, S>>).get())
                    } catch (e: Exception) {
                        throw e
                    }
                }
            })
        }
    }
}