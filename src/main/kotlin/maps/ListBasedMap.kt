package maps

class ListBasedMap<K, V> : CustomMutableMap<K, V> {

    override val entries = CustomLinkedList<Entry<K, V>>()
    override val keys: Iterable<K>
        get() = entries.map { it.key }
    override val values: Iterable<V>
        get() = entries.map { it.value }

    override fun get(key: K): V? {
        if (this.entries.isEmpty()) {
            throw UnsupportedOperationException()
        } else {
            var current = this.entries.root.to
            while (current!!.value.key != key) {
                current = current.to
            }
            return current.value.value
        }
    }

    override fun set(key: K, value: V): V? = put(key, value)

    override fun put(key: K, value: V): V? {
        return if (this.entries.isEmpty()) {
            entries.add(Entry(key, value))
            null
        } else {
            val old: List<Entry<K, V>> = entries.filter { it.key == key }
            if (old.isEmpty()) {
                entries.add(Entry(key, value))
                null
            } else {
                val removed = this.remove(key)
                entries.add(Entry(key, value))
                removed
            }
        }
    }

    override fun put(entry: Entry<K, V>): V? = put(entry.key, entry.value)

    override fun remove(key: K): V? {
        if (this.entries.isEmpty()) {
            throw UnsupportedOperationException()
        } else {
            var current = this.entries.root.to
            while (current!!.value.key != key) {
                current = current.to
            }
            var previous = this.entries.root.to
            while (previous!!.to != current) {
                previous = previous.to
            }
            previous.to = previous.to!!.to
            return current.value.value
        }
    }

    override fun contains(key: K): Boolean = key in keys
}
