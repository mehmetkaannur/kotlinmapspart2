package maps

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

open class StripedGenericHashMap<K, V>(bucketFactory: BucketFactory<K, V>) :
    GenericHashMap<K, V>(bucketFactory) {
    val locks: List<Lock> = (0..<super.buckets.size).map { ReentrantLock() }

    private fun lockFinder(key: K): Lock = locks[key.hashCode() % locks.size]
    override fun put(entry: Entry<K, V>): V? {
        lockFinder(entry.key).withLock {
            return super.put(entry)
        }
    }

    override fun put(key: K, value: V): V? {
        lockFinder(key).withLock {
            return super.put(key, value)
        }
    }

    override fun set(key: K, value: V): V? {
        lockFinder(key).withLock {
            return super.set(key, value)
        }
    }

    override fun remove(key: K): V? {
        lockFinder(key).withLock {
            return super.remove(key)
        }
    }

    override fun contains(key: K): Boolean {
        lockFinder(key).withLock {
            return super.contains(key)
        }
    }

    override fun resize(num: Int) {
        try {
            locks.map { it.lock() }
            super.resize(num)
        } finally {
            locks.map { it.unlock() }
        }
    }

}

class StripedHashMapBackedByLists<K, V>() : StripedGenericHashMap<K, V>({ ListBasedMap() })

class StripedHashMapBackedByTrees<K, V>(keyComparator: Comparator<K>) : StripedGenericHashMap<K, V>({ TreeBasedMap(keyComparator) })
