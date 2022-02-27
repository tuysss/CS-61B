import java.util.*;

public class MyHashMap<K,V> implements Map61B<K,V>{
    private static final int INITIAL_CAPACITY=16;
    private static final double LOAD_FACTOR=0.75;

    private int size;   //number of key-value pairs
    private double loadFactor;   //=buckets.length/size,the limit to resize
    private Item<K,V>[] buckets;    //array of linked-lists(of myHashEntity type)

    private class Item<K,V>{
        private K key;
        private V val;
        private Item next;

        public Item(K key, V val, Item<K,V> next){
            this.key=key;
            this.val=val;
            this.next=next;
        }
    }

    public MyHashMap(){
        this(INITIAL_CAPACITY,LOAD_FACTOR);
    }

    public MyHashMap(int initialSize){
        this(initialSize,LOAD_FACTOR);
    }

    public MyHashMap(int initialSize, double loadFactor){
        buckets=(Item<K, V>[]) new Item[initialSize];
        this.loadFactor=loadFactor;
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        for(int i=0;i< buckets.length;i++){
            buckets[i]=null;
        }
        size=0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return get(key)!=null;
    }

    private int hash(K key){
        return (key.hashCode() & 0x7fffffff)% buckets.length;
    }
    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if(key==null)
            throw new IllegalArgumentException();
        int bucketIndex=hash(key);
        return get(bucketIndex,key);
    }
    private V get(int hashCode, K key){
        Item<K,V> item=buckets[hashCode];
        while(item!=null){
            if(item.key.equals(key)) return item.val;
            item=item.next;
        }
        return null;
    }


    /** Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    @Override
    public void put(K key, V value) {
        int bucketIndex=hash(key);
        if (buckets[bucketIndex]==null){
            buckets[bucketIndex]=new Item<>(key,value,null);
            size++;
        }else{
            put(key,value,buckets[bucketIndex]);
        }
        if(size*loadFactor>=buckets.length) reSize();
    }
    private void put(K key,V value,Item item){
        if(item.key.equals(key)) {
            item.val = value;
        }else if(item.next==null){
            item.next=new Item(key,value,null);
            size++;
        }else{
            put(key,value,item.next);
        }
    }

    private void reSize(){
        MyHashMap<K,V> tempBuckets=new MyHashMap(size()*2);
        Set<K> keys=keySet();
        for(K key:keys){
            tempBuckets.put(key,get(key));
        }
        size= tempBuckets.size();
        buckets=tempBuckets.buckets;
    }

    /** Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet=new HashSet<>();
        for(int i=0;i<buckets.length;i++) {
            Item<K,V> entity=buckets[i];
            while(entity!=null) {
                keySet.add(entity.key);
                entity=entity.next;
            }
        }
        return keySet;
    }


    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }
}
