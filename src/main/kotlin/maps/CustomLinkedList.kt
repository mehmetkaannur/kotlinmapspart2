package maps

import java.util.NoSuchElementException

interface Node<T> {
    var next: Node<T>?
}

class RootNode<T>(override var next: Node<T>?) : Node<T>

class ValueNode<T>(var value: T, override var next: Node<T>?) : Node<T>

class CustomLinkedList<T> : MutableIterable<T> {
    val root: RootNode<T> = RootNode(null)

    fun isEmpty() = root.next == null

    var head: ValueNode<T>
        get() = root.next as ValueNode<T>
        set(newNode) {
            root.next = newNode
        }

    fun add(newValue: T) {
        val newNode: ValueNode<T> = ValueNode(newValue, root.next)
        root.next = newNode
    }

    fun remove(): Node<T>? {
        val removed: Node<T> = root.next ?: return null
        root.next = removed.next
        return removed
    }

    override fun iterator(): MutableIterator<T> {
        return object : MutableIterator<T> {
            var previous: Node<T>? = null
            var current: Node<T> = this@CustomLinkedList.root

            override fun hasNext(): Boolean = current.next != null

            override fun next(): T {
                if (!hasNext()) {
                    throw NoSuchElementException()
                }
                previous = current
                current = current.next!!
                return (current as ValueNode).value
            }

            override fun remove() {
                if (!this@CustomLinkedList.isEmpty()) {
                    throw UnsupportedOperationException()
                } else if (previous == this@CustomLinkedList.root) {
                    current = this@CustomLinkedList.root
                    previous = null
                } else {
                    previous!!.next = current.next
                }
            }
        }
    }
}
