package bearmaps;

import java.util.ArrayList;
import java.util.HashMap;

public class ArrayHeapMinPQ<T> implements ExtrinsicMinPQ<T>{
    private ArrayList<Node<T>> itemPQ;    //store items from 0 to n-1
    private HashMap<T,Integer> itemIndexMap;


    public ArrayHeapMinPQ(){
        itemPQ =new ArrayList<>();
        itemIndexMap=new HashMap<>();
    }

    @Override
    public void add(T item, double priority) {
        if(contains(item))
            throw new IllegalArgumentException("Item already exists in priority queue.");
        itemPQ.add(new Node(item,priority));
        itemIndexMap.put(item,size()-1);
        swim(size()-1);
    }


    @Override
    public boolean contains(T item) {
        return itemIndexMap.containsKey(item);
    }
    private boolean isEmpty(){
        return size()==0;
    }


    @Override
    public T getSmallest() {
        if(isEmpty())
            throw new IllegalArgumentException("The priority queue is empty.");
        return itemPQ.get(0).getItem();
    }

    @Override
    public T removeSmallest() {
        T toRemove=getSmallest();
        itemPQ.set(0, itemPQ.get(size()-1));
        //itemIndexMap.put(itemPQ.get(0).getItem(),0);
        itemIndexMap.remove(toRemove);
        itemPQ.remove(size()-1);
        sink(0);
        return toRemove;
    }

    @Override
    public int size() {
        return itemPQ.size();
    }

    @Override
    public void changePriority(T item, double priority) {
        if(!contains(item))
            throw new IllegalArgumentException();
        int index=itemIndexMap.get(item);
        itemPQ.get(index).setPriority(priority);
        swim(index);
        sink(index);
    }


    private void swim(int k){
        if(Double.compare(itemPQ.get(k).getPriority(), itemPQ.get(parent(k)).getPriority())<0){
            swap(k,parent(k));
            swim(parent(k));
        }
    }

    private void sink(int k){
        int smallerIndex=k;
        if(leftChild(k)<=size()-1&&Double.compare(itemPQ.get(k).getPriority(), itemPQ.get(leftChild(k)).getPriority())>0)
             smallerIndex=leftChild(k);
        if(rightChild(k)<=size()-1&&Double.compare(itemPQ.get(smallerIndex).getPriority(), itemPQ.get(rightChild(k)).getPriority())>0)
            smallerIndex=rightChild(k);
        if(smallerIndex!=k){
            swap(smallerIndex,k);
            sink(smallerIndex);
        }
    }

    private int parent(int k){
        if(k==0) return 0;
        else return (k-1)/2;
    }
    private int leftChild(int k){
        return 2*k+1;
    }
    private int rightChild(int k){
        return 2*k+2;
    }

    private void swap(int x,int y){
        Node temp= itemPQ.get(x);
        itemPQ.set(x, itemPQ.get(y));
        itemPQ.set(y,temp);
        itemIndexMap.put(itemPQ.get(x).getItem(),x);
        itemIndexMap.put(itemPQ.get(y).getItem(),y);
    }


    private class Node<T>{
        private T item;
        private double priority;

        public Node(T item,double piority){
            this.item=item;
            this.priority =piority;
        }

        public T getItem() {
            return item;
        }
        public void setItem(T item) {
            this.item = item;
        }
        public double getPriority() {
            return priority;
        }
        public void setPriority(double priority) {
            this.priority = priority;
        }
    }



}
