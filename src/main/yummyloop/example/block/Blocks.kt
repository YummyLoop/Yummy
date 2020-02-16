package yummyloop.example.block

import net.minecraft.block.Block as VanillaBlock

object Blocks : HashMap<String, VanillaBlock>() {

    fun ini() {
        //TemplateBlockWithEntity("template_be", Block.of(Material.METAL).lightLevel(10))
        //Barrel("test_barrel", Block.of(Material.BAMBOO))
    }
}