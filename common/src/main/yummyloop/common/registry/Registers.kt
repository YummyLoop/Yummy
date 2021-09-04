package yummyloop.common.registry

import dev.architectury.networking.NetworkManager
import dev.architectury.registry.block.BlockProperties
import dev.architectury.registry.menu.MenuRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttribute
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import yummyloop.api.archi.entity.attribute.EntityAttributeLinkRegister
import yummyloop.common.Common.LOG
import yummyloop.yummy.item.Ytem
import java.util.function.Supplier

/** Contains the declared registers, and the functions to register new content */
class Registers(private val modId: String) {
    val client by lazy { ClientRegisters(modId) }

    /** Register map, with priority, and register function */
    private val registerMap by lazy { mutableMapOf<Int, () -> Unit>() }

    /** Final registry of the content */
    internal fun register() {
        LOG.info("Registering mod content")
        registerMap.toSortedMap().onEach { it.value.invoke() }.clear()
    }

    /** Block register */
    private val blockRegister: DeferredRegister<Block> by lazy {
        DeferredRegister.create(modId, Registry.BLOCK_KEY).also { registerMap[3] = it::register }
    }

    /** Block Entity Type register */
    private val blockEntityTypeRegister: DeferredRegister<BlockEntityType<*>> by lazy {
        DeferredRegister.create(modId, Registry.BLOCK_ENTITY_TYPE_KEY).also { registerMap[4] = it::register }
    }

    /** Entity Type register */
    private val entityTypeRegister: DeferredRegister<EntityType<*>> by lazy {
        DeferredRegister.create(modId, Registry.ENTITY_TYPE_KEY).also { registerMap[2] = it::register }
    }

    /** Entity Attribute register */
    private val entityAttributeRegister: DeferredRegister<EntityAttribute> by lazy {
        DeferredRegister.create(modId, Registry.ATTRIBUTE_KEY).also { registerMap[1] = it::register }
    }

    /** Entity Attribute link register */
    private val entityAttributeLinkRegister: EntityAttributeLinkRegister by lazy {
        EntityAttributeLinkRegister.create(modId).also { registerMap[6] = it::register }
    }

    /** Item register */
    private val itemRegister: DeferredRegister<Item> by lazy {
        DeferredRegister.create(modId, Registry.ITEM_KEY).also { registerMap[5] = it::register }
    }

    /** Menu register */
    private val screenHandlerTypeRegister: DeferredRegister<ScreenHandlerType<*>> by lazy {
        DeferredRegister.create(modId, Registry.MENU_KEY).also { registerMap[7] = it::register }
    }

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
        blockSupplier: () -> Block = { Block(BlockProperties.of(Material.SOIL)) },
    ): RegistrySupplier<Block> = blockRegister.register(blockId, blockSupplier)

    /**
     * Registers a new block and a corresponding custom item
     *
     * @param blockItemId Id of the block and item
     * @param blockSupplier The supplier used to create the block
     * @param itemSettings The item settings used to create the item
     * @return A Pair of RegistrySuppliers for the block and item
     */
    fun blockItem(
        blockItemId: String,
        blockSupplier: () -> Block = { Block(BlockProperties.of(Material.SOIL)) },
        blockItemSupplier: (Block, Item.Settings) -> BlockItem = ::BlockItem,
    ): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
        val block = this.block(blockItemId, blockSupplier)
        val item = this.item(blockItemId) { blockItemSupplier.invoke(block.get(), Ytem.Settings()) }
        return Pair(block, item)
    }

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
        blockSupplier: () -> Block = { Block(BlockProperties.of(Material.SOIL)) },
    ): Pair<RegistrySupplier<Block>, RegistrySupplier<Item>> {
        return blockItem(blockItemId, blockSupplier, ::BlockItem)
    }

    /**
     * Registers/Associates a blockEntityType with multiple blocks
     *
     * @param blockEntityTypeId Id of the block entity type
     * @param blockEntityTypeSupplier The supplier used to create the block entity
     * @param blocks The blocks to be associated with the block entity type
     * @return A RegistrySupplier for the block entity type
     */
    // Todo : Fix this ?
    /**
    fun <T> blockEntityType(
        blockEntityTypeId: String,
        vararg blocks: RegistrySupplier<Block>,
        blockEntityTypeSupplier: Supplier<T>,
    ): RegistrySupplier<BlockEntityType<T>> where T : BlockEntity {
        return blockEntityTypeRegister.register(blockEntityTypeId) {
            BlockEntityType.Builder.create(
                blockEntityTypeSupplier,
                *blocks.map { it.get() }.toTypedArray()
            ).build(null)
        }
    }*/

    /**
     * Registers an EntityType
     *
     * @param entityTypeId Id of the entity
     * @param entityTypeBuilder The builder used to create the entity
     * @return A RegistrySupplier for the entity type
     */
    fun <T> entityType(
        entityTypeId: String,
        entityTypeBuilder: () -> (EntityType.Builder<T>),
    ): RegistrySupplier<EntityType<T>> where T : Entity {
        return entityTypeRegister.register(entityTypeId) { entityTypeBuilder.invoke().build(entityTypeId) }
    }

    /**
     * Registers an EntityType
     *
     * @param entityId Id of the entity
     * @param entityFactory The factory used to create the entity
     * @param spawnGroup The entity spawnGroup
     * @param entityBuilderModifications The optional options to append to the entity builder
     * @return A RegistrySupplier for the entity type
     */
    fun <T> entityType(
        entityId: String,
        entityFactory: (type: EntityType<T>, world: World) -> (T),
        spawnGroup: SpawnGroup = SpawnGroup.CREATURE,
        entityBuilderModifications: (append: EntityType.Builder<T>) -> (EntityType.Builder<T>) = { it },
    ): RegistrySupplier<EntityType<T>> where T : Entity {
        val create = EntityType.Builder.create(entityFactory, spawnGroup)
        val append = entityBuilderModifications.invoke(create)
        return entityTypeRegister.register(entityId) { append.build(entityId) }
    }

    /**
     * Registers an EntityType with attributes
     *
     * @param entityId Id of the entity
     * @param entityFactory The factory used to create the entity
     * @param spawnGroup The entity spawnGroup
     * @param entityAttributeBuilder The entity attribute builder
     * @param entityBuilderModifications The optional options to append to the entity builder
     * @return A RegistrySupplier for the entity type
     */
    fun <T> entityTypeWithAttributes(
        entityId: String,
        entityFactory: (type: EntityType<T>, world: World) -> (T),
        spawnGroup: SpawnGroup = SpawnGroup.CREATURE,
        entityAttributeBuilder: () -> DefaultAttributeContainer.Builder = MobEntity::createMobAttributes,
        entityBuilderModifications: (append: EntityType.Builder<T>) -> (EntityType.Builder<T>) = { it },
    ): RegistrySupplier<EntityType<T>> where T : LivingEntity {
        return entityType(entityId, entityFactory, spawnGroup, entityBuilderModifications)
            .also { entityAttributeLink(it, entityAttributeBuilder) }
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
        entityAttributeBuilder: () -> DefaultAttributeContainer.Builder = MobEntity::createMobAttributes,
    ) {
        entityAttributeLinkRegister.register(entityType, entityAttributeBuilder)
    }

    /**
     * Registers a ScreenHandlerType
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param handlerFactory The factory of the ScreenHandler [H]
     * @return RegistrySupplier of the type of the screenHandler [H]
     */
    fun <H> screenHandlerType(
        screenHandlerTypeId: String,
        handlerFactory: Supplier<ScreenHandlerType<out H>>,
    ): RegistrySupplier<ScreenHandlerType<out H>> where H : ScreenHandler {
        return screenHandlerTypeRegister.register(screenHandlerTypeId, handlerFactory)
    }

    /**
     * Registers a Simple ScreenHandlerType
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param handlerFactory The factory of the ScreenHandler [H]
     * @return RegistrySupplier of the type of the screenHandler [H]
     */
    fun <H> screenHandlerTypeSimple(
        screenHandlerTypeId: String,
        handlerFactory: MenuRegistry.SimpleMenuTypeFactory<H>,
    ): RegistrySupplier<ScreenHandlerType<out H>> where H : ScreenHandler {
        return screenHandlerType(screenHandlerTypeId) { MenuRegistry.of(handlerFactory) }
    }

    /**
     * Registers a Simple ScreenHandlerType, ScreenHandler and Screen
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param handlerFactory The factory of the ScreenHandler [H]
     * @param screenFactory The factory of the Screen [S]
     * @return RegistrySupplier of the type of the screenHandler [H]
     */
    fun <H, S> screenHandlerTypeSimple(
        screenHandlerTypeId: String,
        handlerFactory: MenuRegistry.SimpleMenuTypeFactory<H>,
        screenFactory: () -> ((H, PlayerInventory, Text) -> S),
    ): RegistrySupplier<ScreenHandlerType<out H>> where H : ScreenHandler, S : Screen, S : ScreenHandlerProvider<H> {
        return screenHandlerTypeSimple(screenHandlerTypeId, handlerFactory).also { client.screen(it, screenFactory) }
    }

    /**
     * Registers an Extended ScreenHandlerType
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param handlerFactory The factory of the ScreenHandler [H]
     * @return RegistrySupplier of the type of the screenHandler [H]
     */
    fun <H> screenHandlerTypeExtended(
        screenHandlerTypeId: String,
        handlerFactory: MenuRegistry.ExtendedMenuTypeFactory<H>,
    ): RegistrySupplier<ScreenHandlerType<out H>> where H : ScreenHandler {
        return screenHandlerType(screenHandlerTypeId) { MenuRegistry.ofExtended(handlerFactory) }
    }

    /**
     * Registers an Extended ScreenHandlerType, ScreenHandler and Screen
     *
     * @param screenHandlerTypeId Id of the ScreenHandlerType
     * @param handlerFactory The factory of the ScreenHandler [H]
     * @param screenFactory The factory of the Screen [S]
     * @return RegistrySupplier of the type of the screenHandler [H]
     */
    fun <H, S> screenHandlerTypeExtended(
        screenHandlerTypeId: String,
        handlerFactory: MenuRegistry.ExtendedMenuTypeFactory<H>,
        screenFactory: () -> ((H, PlayerInventory, Text) -> S),
    ): RegistrySupplier<ScreenHandlerType<out H>> where H : ScreenHandler, S : Screen, S : ScreenHandlerProvider<H> {
        return screenHandlerTypeExtended(screenHandlerTypeId, handlerFactory).also { client.screen(it, screenFactory) }
    }

    /**
     * Registers behaviour ([receiver]) to take when the Server receives a named packet ([packetName]) from a Client
     *
     * @param packetName the name of the received packet
     * @param receiver the actions to take when the packet is received
     */
    fun onReceivePacketFromClient(packetName: String, receiver: NetworkManager.NetworkReceiver) {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, Identifier(modId, packetName), receiver)
    }

    /**
     * Registers behaviour ([receiver]) to take when the Server receives a named packet ([packetName]) from a Client
     *
     * @param packetName the name of the received packet
     * @param receiver the actions to take when the packet is received
     */
    fun onReceivePacketFromClient(packetName: String, receiver: (PacketByteBuf, NetworkManager.PacketContext) -> Unit) {
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, Identifier(modId, packetName), receiver)
    }
}