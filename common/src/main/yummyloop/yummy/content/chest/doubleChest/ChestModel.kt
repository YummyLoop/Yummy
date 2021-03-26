package yummyloop.yummy.content.chest.doubleChest

import yummyloop.yummy.content.chest.singleChest.SingleChestEntity

class ChestModel(textureName: String) : AbstractChestModel<SingleChestEntity>(textureName) {
    companion object {
        val DEFAULT by lazy { ChestModel("chest") }
    }
}