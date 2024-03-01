package maps

import java.util.NoSuchElementException

interface Node<T> {
    var to: ValueNode<T>?
}

class RootNode<T>(override var to: ValueNode<T>? = null) : Node<T>

class ValueNode<T>(val value: T, override var to: ValueNode<T>? = null) :
    Node<T>

class CustomLinkedList<T>(val root: RootNode<T> = RootNode()) :
    MutableIterable<T> {
    fun isEmpty() = root.to == null

    fun head() = this.root.to

    fun add(new_value: T) {
        val newNode: ValueNode<T> = ValueNode(new_value, this.root.to)
        this.root.to = newNode
    }

    fun remove(): T {
        if (this.isEmpty()) {
            throw UnsupportedOperationException()
        } else {
            val removed = this.root.to!!.value
            this.root.to = this.root.to!!.to
            return removed
        }
    }

    override fun iterator(): MutableIterator<T> {
        return object : MutableIterator<T> {
            var current: Node<T>? = root
            var lastReturn: Node<T>? = null

            override fun hasNext(): Boolean = current?.to != null

            override fun next(): T {
                if (!hasNext()) {
                    throw NoSuchElementException()
                } else {
                    lastReturn = current?.to
                    current = current?.to
                    return (current as ValueNode<T>?)?.value!!
                }
            }

            override fun remove() {
                if (lastReturn == null || lastReturn == root) {
                    throw UnsupportedOperationException()
                } else {
                    var tempNode: Node<T> = root
                    while (tempNode.to != lastReturn) {
                        tempNode =
                            tempNode.to ?: throw IllegalStateException()
                    }
                    tempNode.to = lastReturn!!.to
                    lastReturn
                }
            }
        }
    }
}
