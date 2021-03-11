package yummyloop.common.inventory

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

interface IterableInventory : List<ItemStack>, Inventory {
    override val size: Int
        get() = size


    override fun contains(element: ItemStack): Boolean {
        for (i in 0 until this.size) {
            if (ItemStack.areEqual(this.getStack(i), element))
                return true
        }
        return false
    }


    override fun containsAll(elements: Collection<ItemStack>): Boolean {
        for (i in elements) {
            if (!this.contains(i)) {
                return false
            }
        }
        return true
    }

    /**
     * Returns the element at the specified index in the list.
     */
    override fun get(index: Int): ItemStack {
        return this.getStack(index)
    }

    /**
     * Returns the index of the first occurrence of the specified element in the list, or -1 if the specified
     * element is not contained in the list.
     */
    override fun indexOf(element: ItemStack): Int {
        for (i in 0 until this.size) {
            if (ItemStack.areEqual(this.getStack(i), element))
                return i
        }
        return -1
    }

    override fun iterator(): Iterator<ItemStack> {
        return object : AbstractIterator<ItemStack>() {
            var i = 0

            /**
             * Computes the next item in the iterator.
             *
             * This callback method should call one of these two methods:
             *
             * * [setNext] with the next value of the iteration
             * * [done] to indicate there are no more elements
             *
             * Failure to call either method will result in the iteration terminating with a failed state
             */
            override fun computeNext() {
                while (i < size()) {
                    setNext(getStack(i++))
                    return
                }
                done()
            }
        }
    }

    /**
     * Returns the index of the last occurrence of the specified element in the list, or -1 if the specified
     * element is not contained in the list.
     */
    override fun lastIndexOf(element: ItemStack): Int {
        for (i in this.size - 1 downTo 0) {
            if (ItemStack.areEqual(this.getStack(i), element))
                return i
        }
        return -1
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence).
     */
    override fun listIterator(): ListIterator<ItemStack> {
        return object : ListIterator<ItemStack> {
            var n = 0
            override fun hasNext(): Boolean = n < size()

            /**
             * Returns `true` if there are elements in the iteration before the current element.
             */
            override fun hasPrevious(): Boolean = n > 0

            override fun next(): ItemStack = getStack(n++)

            /**
             * Returns the index of the element that would be returned by a subsequent call to [next].
             */
            override fun nextIndex(): Int = n

            /**
             * Returns the previous element in the iteration and moves the cursor position backwards.
             */
            override fun previous(): ItemStack = getStack(--n)

            /**
             * Returns the index of the element that would be returned by a subsequent call to [previous].
             */
            override fun previousIndex(): Int = n - 1
        }
    }

    /**
     * Returns a list iterator over the elements in this list (in proper sequence), starting at the specified [index].
     */
    override fun listIterator(index: Int): ListIterator<ItemStack> {
        return object : ListIterator<ItemStack> {
            var i = index
            override fun hasNext(): Boolean = i < size()

            /**
             * Returns `true` if there are elements in the iteration before the current element.
             */
            override fun hasPrevious(): Boolean = i > 0

            override fun next(): ItemStack = getStack(i++)

            /**
             * Returns the index of the element that would be returned by a subsequent call to [next].
             */
            override fun nextIndex(): Int = i

            /**
             * Returns the previous element in the iteration and moves the cursor position backwards.
             */
            override fun previous(): ItemStack = getStack(--i)

            /**
             * Returns the index of the element that would be returned by a subsequent call to [previous].
             */
            override fun previousIndex(): Int = i - 1

        }
    }

    /**
     * Returns a view of the portion of this list between the specified [fromIndex] (inclusive) and [toIndex] (exclusive).
     * The returned list is backed by this list, so non-structural changes in the returned list are reflected in this list, and vice-versa.
     *
     * Structural changes in the base list make the behavior of the view undefined.
     */
    override fun subList(fromIndex: Int, toIndex: Int): List<ItemStack> {
        val list = mutableListOf<ItemStack>()
        for (i in fromIndex until toIndex) {
            list.add(this.getStack(i))
        }
        return list
    }
}