package maps

class LockedHashMapBackedByLists<K, V> : LockedMap<K, V>(HashMapBackedByLists<K, V>())