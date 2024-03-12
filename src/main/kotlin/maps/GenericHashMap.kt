package maps

typealias BucketFactory<K, V> = () -> CustomMutableMap<K, V>

val NORMAL_ARRAY_SIZE = 32
val LOAD_FACTOR = 0.75

abstract class GenericHashMap<K, V>(private val bucketFactory: BucketFactory<K, V>) :
    CustomMutableMap<K, V> {
    protected var buckets: Array<CustomMutableMap<K, V>> = Array(NORMAL_ARRAY_SIZE) { bucketFactory() }
    override val entries: Iterable<Entry<K, V>>
        get() = buckets.flatMap { it.entries }
    override val keys: Iterable<K>
        get() = entries.map { it.key }
    override val values: Iterable<V>
        get() = entries.map { it.value }

    private val capacity
        get() = buckets.size

    var size = 0
        set(value) {
            field = value
            if (value > capacity * LOAD_FACTOR) {
                this.resize(capacity * 2)
            }
        }

    protected open fun resize(num: Int) {
        val oldEntries = this.entries
        buckets = Array(num) { bucketFactory() }
        oldEntries.forEach { put(it) }
    }

    override fun get(key: K): V? {
        val hashKey = key.hashCode() % buckets.size
        return buckets[hashKey][key]
    }

    override fun set(
        key: K,
        value: V,
    ): V? {
        val hashKey = key.hashCode() % buckets.size
        val setted = buckets[hashKey].set(key, value)
        if (setted == null) {
            size++
        }
        return setted
    }

    override fun put(
        key: K,
        value: V,
    ): V? {
        return set(key, value)
    }

    override fun put(entry: Entry<K, V>): V? {
        return put(entry.key, entry.value)
    }

    override fun remove(key: K): V? {
        val hashKey = key.hashCode() % buckets.size
        val removed = buckets[hashKey].remove(key)
        if (removed != null) {
            size--
        }
        return removed
    }

    override fun contains(key: K): Boolean {
        val hashKey = key.hashCode() % buckets.size
        return buckets[hashKey].contains(key)
    }
}

class HashMapBackedByLists<K, V>() : GenericHashMap<K, V>({ ListBasedMap() })
