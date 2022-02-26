import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    Set<K> KeySet=new HashSet<>();

    private class Node{
        private K key;
        private V value;
        private Node left,right;
        private int size;

        public Node(K key,V value,int size){
            this.key=key;
            this.value=value;
            this.size=size;
        }
    }
    public BSTMap(){

    }

    public BSTMap(K key,V value){
        root=new Node(key,value,0);
    }

    /** Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root=null;
    }

    /* Returns true if this map contains a mapping for the specified key. */
    @Override
    public boolean containsKey(K key) {
        return get(key)!=null;
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return get(key,root);
    }

    private V get(K key,Node x){
        if(x==null) return null;
        int cmp=key.compareTo(x.key);// cmp: ascii差值
        if(cmp<0) return get(key,x.left);
        if(cmp>0) return get(key,x.right);
        return x.value;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size(root);
    }
    private int size(Node x){
        if(x==null) return 0;
        else return x.size;
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        root= put(key,value,root);
    }

    private Node put(K key, V value, Node x){
        if(x==null) return new Node(key,value,1);
        int cmp=key.compareTo(x.key);
        if(cmp<0)       x.left= put(key,value,x.left);
        else if(cmp>0)  x.right= put(key,value,x.right);
        else  x.value=value;
        x.size=size(x.left)+size(x.right)+1;
        return x;
    }

    /** Print out BSTMap in order of increasing key.*/
    public void printInOrder(){
        printInOrder(root);
    }
    private void printInOrder(Node x){  //ni mid order
        if(x==null) return ;
        printInOrder(x.left);
        System.out.println(x.key+": "+x.value);
        printInOrder(x.right);
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        preOrder(root);
        return KeySet;
    }
    private Set<K> preOrder(Node x){
        if(x==null) return null;
        KeySet.add(x.key);
        preOrder(x.left);
        preOrder(x.right);
        return KeySet;
    }

    public K min(){
        return min(root).key;
    }
    private Node min(Node x){
        if(x.left==null) return x;
        else return min(x.left);
    }

    //向下取整
    public K floor(K key){
        return floor(key,root).key;
    }
    private Node floor(K key,Node x){
        if(x==null) return null;
        int cmp=key.compareTo(x.key);
        //if(cmp==0) return x;
        /* key<x.key : floor(key)一定在x的左子树中 */
        if(cmp<0) floor(key,x.left);
        /* key>x.key : floor(key)在x的右子树的左子树中 or floor(key)=x.key */
        Node t=floor(key,x.right);
        if(t!=null) return t;
        return x;
    }

    public K max(){
        return min(root).key;
    }
    private Node max(Node x){
        if(x.right==null) return x;
        else return max(x.right);
    }

    //向上取整
    public K ceiling(K key){
        return ceiling(key,root).key;
    }
    private Node ceiling(K key,Node x){
        if(x==null) return null;
        int cmp= key.compareTo(x.key);
        if(cmp>0) return ceiling(key,x.right);
        Node t=ceiling(key,x.left);
        if(t!=null) return t;
        return x;
    }

    /* 删除后，需要改变树的结构 */
    public void deleteMin(){
        root=deleteMin(root);
    }
    private Node deleteMin(Node x){
        /* 不断向左子树找，直到遇见空链接。将指向该结点x的链接改指向x.right */
        if(x.left==null) return x.right;  //如果是叶子结点，return null没有影响
        x.left=deleteMin(x.left);
        x.size=size(x.left)+size(x.right)-1;
        return x;
    }


    @Override
    public V remove(K key) {
        if(!containsKey(key))
            throw new IllegalArgumentException();
        V toRemove=get(key);
        root=remove(key,root);
        return toRemove;
    }

    @Override
    public V remove(K key, V value) {
        if(!containsKey(key))
            throw new IllegalArgumentException();
        if(value!=get(key))
            throw new IllegalArgumentException();
        V toRemove=get(key);
        root=remove(key,root);
        return toRemove;
    }


    private Node remove(K key,Node x){
        if(x==null) return null;
        int cmp=key.compareTo(x.key);
        if(cmp<0)       x.left=remove(key,x.left);
        else if(cmp>0)  x.right=remove(key,x.right);
        else{//cmp==0
            if(x.right==null) return x.left;
            if(x.left==null)  return x.right;
            //else:
            Node t=x;
            /*  x（待删除结点）指向x的后继结点  */
            x=min(t.right);
            /*  x的右链接指向删除x后所有节点仍大于x.key的子树  */
            x.right=deleteMin(t.right);
            x.left=t.left;
        }
        x.size=size(x.left)+size(x.right)+1;
        return x;
    }





    @Override
    public Iterator<K> iterator() {
        return new BTSMapIterator();
    }

    private class BTSMapIterator implements Iterator<K>{
        int i=0;

        @Override
        public boolean hasNext() {
            return i< keySet().size();
        }

        @Override
        public K next() {
            K keyArray[]=(K[])keySet().toArray();
            K returnKey=keyArray[i];
            i++;
            return returnKey;
        }
    }
}