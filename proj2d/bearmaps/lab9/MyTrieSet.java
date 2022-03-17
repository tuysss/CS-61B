package bearmaps.lab9;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyTrieSet implements TrieSet61B{
    private TrieNode root;

    private class TrieNode{
        char nodeChar;
        HashMap<Character, TrieNode> children;
        boolean isLeaf;

        TrieNode(char nodeChar,boolean isLeaf){
            children=new HashMap<>();
            this.nodeChar=nodeChar;
            this.isLeaf=isLeaf;
        }
    }

    public MyTrieSet(){
        root=new TrieNode('\0',false);   // NUL in ASCII
    }


    @Override
    public void clear() {
        root=null;
    }

    @Override
    public boolean contains(String key) {
        if(root==null||key==null||key.length()==0){
            return false;
        }
        TrieNode currNode=root;
        TrieNode nextNode=null;
        for(int i=0;i<key.length();i++){
            char c=key.charAt(i);
            nextNode=currNode.children.get(c);
            if(nextNode==null){
                return false;
            }
            currNode=nextNode;
        }
        return currNode.isLeaf;
    }

    @Override
    public void add(String key) {
        if(root==null||key==null||key.length()==0){
            return ;
        }
        TrieNode currNode=root;
        for(int i=0;i<key.length();i++){
            char c=key.charAt(i);
            if(!currNode.children.containsKey(c)){
                currNode.children.put(c,new TrieNode(c,false));
            }
            currNode= currNode.children.get(c);
        }
        currNode.isLeaf=true;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        if(prefix==null||prefix.length()==0||root==null){
            throw new IllegalArgumentException();
        }
        List<String> result=new ArrayList<>();
        TrieNode startNode=root;
        for(int i=0;i<prefix.length();i++){
            char c=prefix.charAt(i);
            if(!startNode.children.containsKey(c)){
                throw new IllegalArgumentException();
            }
            startNode=startNode.children.get(c);
        }
        if(startNode.isLeaf){
            result.add(prefix);
        }
        for(TrieNode currNode:startNode.children.values()){
            if(currNode!=null){
                keysWithPrefix(currNode,prefix,result);
            }
        }
        return result;
    }

    private void keysWithPrefix(TrieNode currNode,String word,List<String> result){
        if(currNode.isLeaf){
            result.add(word+currNode.nodeChar);
        }
        for(TrieNode nextNode:currNode.children.values()){
            if(nextNode!=null){
                keysWithPrefix(nextNode,word+currNode.nodeChar,result);
            }
        }
    }


    @Override
    public String longestPrefixOf(String key) {
        StringBuffer longestPrefix = new StringBuffer();
        TrieNode currNode = root;
        for (int i = 0; i < key.length(); i += 1) {
            char c = key.charAt(i);
            if (!currNode.children.containsKey(c)) {
                return longestPrefix.toString();
            } else {
                longestPrefix.append(c);
                currNode = currNode.children.get(c);
            }
        }
        return longestPrefix.toString();
    }

    /*
    *longest-common-prefix
    private boolean haveSiblings(TrieNode mother){
        int count=0;
        for(TrieNode child:mother.children.values()){
            count++;
        }
        return count>1;
    }
     */
}
