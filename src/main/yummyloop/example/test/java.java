package yummyloop.example.test;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;
import yummyloop.example.item.Items;

public class java {
    public static BlockEntityType<DemoBlockEntity> DEMO_BLOCK_ENTITY;

    public class DemoBlockEntity extends BlockEntity {
        public DemoBlockEntity() {
            super(DEMO_BLOCK_ENTITY);
        }
    }

    java(){
        DEMO_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY, "modid:demo", BlockEntityType.Builder.create(DemoBlockEntity::new, Items.blockA).build(null));
    }
}
