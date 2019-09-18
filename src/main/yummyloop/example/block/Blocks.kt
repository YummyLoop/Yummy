package yummyloop.example.block

import net.minecraft.block.Material
import net.minecraft.entity.EquipmentSlot
import net.minecraft.item.ArmorMaterials
import yummyloop.example.block.Block
import yummyloop.example.block.entity.TemplateBlockEntity
import yummyloop.example.block.entity.TemplateBlockWithEntity
import yummyloop.example.item.backpack.BContainer
import yummyloop.example.item.backpack.Backpack
import net.minecraft.block.Block as VanillaBlock
import net.minecraft.item.Item as VanillaItem
import net.minecraft.item.Item.Settings as ItemSettings
import net.minecraft.item.Items as Vanilla

object Blocks : HashMap<String, VanillaBlock>() {

    fun ini() {
        TemplateBlockWithEntity("template_be", Block.of(Material.METAL).lightLevel(10))

        TemplateBlockEntity
    }
}