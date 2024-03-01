package maps

abstract class GenericHashMap<K, V>(private val bucketFactory: () -> CustomMutableMap<K, V>) :
    CustomMutableMap<K, V> {
    private var capacity = 16
    private var size: Int = 0
    private val loadFactor: Int = 2
    private var buckets: Array<CustomMutableMap<K, V>> =
        Array(capacity) { bucketFactory() }

    override val entries: Iterable<Entry<K, V>>
        get() = buckets.flatMap { it.entries }
    override val keys: Iterable<K>
        get() = entries.map { it.key }
    override val values: Iterable<V>
        get() = entries.map { it.value }

    override fun contains(key: K): Boolean {
        val bucketIndex = key.hashCode() % capacity
        return buckets[bucketIndex].contains(key)
    }

    override fun remove(key: K): V? {
        val bucketIndex = key.hashCode() % capacity
        val removedValue = buckets[bucketIndex].remove(key)
        if (removedValue != null) size--
        return removedValue
    }

    override fun put(entry: Entry<K, V>): V? = put(entry.key, entry.value)

    override fun put(key: K, value: V): V? {
        val bucketIndex = key.hashCode() % capacity
        val returnValue = buckets[bucketIndex].put(key, value)
        size++
        if (size == capacity) {
            val prevEntries = entries

            // increase capacity
            capacity *= loadFactor
            buckets = Array(capacity) { bucketFactory() }

            prevEntries.forEach { put(it) }
        }
        return returnValue
    }

    override fun set(key: K, value: V): V? = put(key, value)

    override fun get(key: K): V? {
        val bucketIndex = key.hashCode() % capacity
        return buckets[bucketIndex][key]
    }
}

class HashMapBackedByLists2<K, V> : GenericHashMap<K, V>({ ListBasedMap() })

fun main() {
    val hml = HashMapBackedByLists2<String, Int>()
    val entries = (1..10).map {
        Entry(it.toString(), it)
    }
    entries.forEach(hml::put)
    val values = entries.map { it.value }

    println(values)
    println(hml.values.sorted())
}
