package maps

class ListBasedMap<K, V> : CustomMutableMap<K, V> {
    override var entries: Iterable<Entry<K, V>> = CustomLinkedList()
    override val keys: Iterable<K>
        get() = entries.map { it.key }
    override val values: Iterable<V>
        get() = entries.map { it.value }

    override fun get(key: K): V? {
        if (key in keys) {
            return entries.first { it.key == key }.value
        }
        return null
    }

    override fun set(
        key: K,
        value: V,
    ): V? {
        var removed: V? = null

        if (contains(key)) {
            removed = this.remove(key)
        }
        (entries as CustomLinkedList<Entry<K, V>>).add(Entry(key, value))
        return removed
    }

    override fun put(
        key: K,
        value: V,
    ): V? {
        return this.set(key, value)
    }

    override fun put(entry: Entry<K, V>): V? {
        return put(entry.key, entry.value)
    }

    override fun remove(key: K): V? {
        if (!contains(key)) {
            return null
        } else {
            val removed = entries.first { it.key == key }
            val temp: CustomLinkedList<Entry<K, V>> = CustomLinkedList()
            entries.filter { it.key != key }.forEach { temp.add(it) }
            entries = temp
            return removed.value
        }
    }

    override fun contains(key: K): Boolean = key in keys
}
