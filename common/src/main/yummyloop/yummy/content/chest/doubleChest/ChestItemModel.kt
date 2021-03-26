package yummyloop.yummy.content.chest.doubleChest

class ChestItemModel private constructor(textureName: String) : AbstractChestModel<ChestItem>(textureName) {
    companion object {
        val DEFAULT by lazy { ChestItemModel("chest") }
    }
}