package com.springapp.mvc.autocomplete;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 16/8/13
 * Time: 8:59 PM
 * To change this template use File | Settings | File Templates.
 */

public class Trie
{
    public TrieNode root;


    public Trie() {
        this.root = new TrieNode('\0', false);
    }


    public void insertWord(TrieNode root, String word)
    {
        int l = word.length();
        char[] letters = word.toCharArray();
        TrieNode curNode = root;

        for (int i = 0; i < l; i++)
        {
            if (curNode.map.containsKey(letters[i]) == false)
                curNode.map.put(letters[i],new TrieNode(letters[i], i == l-1 ? true : false));
            curNode = (TrieNode) curNode.map.get(letters[i]);
        }
    }

    public boolean find(TrieNode root, String word)

    {
        char[] letters = word.toCharArray();
        int l = letters.length;
        TrieNode curNode = root;

        int i;
        for (i = 0; i < l; i++)
        {
            if (curNode == null)
                return false;
            curNode = (TrieNode) curNode.map.get(letters[i]);
        }

        if (i == l && curNode == null)
            return false;

        if (curNode != null && !curNode.fullWord)
            return false;

        return true;
    }

    public TrieNode getPrefixNode(String word, StringBuilder testword) {
        TrieNode cur = root;
        int i=0;
        while ( i < word.length()){
            if (cur.map.get(word.charAt(i)) == null)
                return cur;    // only a strict prefix exists which is a path
            else
                cur = (TrieNode) cur.map.get(word.charAt(i));
            testword.append(word.charAt(i));
            i++;
        }
        return cur;
    }

    public ArrayList<String> getAllPrefixMatches(String prefix)
    {
        StringBuilder testword = new StringBuilder();
        TrieNode node = getPrefixNode(prefix, testword);
        ArrayList<String> stringList = new ArrayList<String>();
       if (testword.length() == prefix.length())
            preOrderTraverse(node,stringList,prefix);

        return stringList;
    }



    public void preOrderTraverse(TrieNode node, ArrayList<String> list, String word){
        if ( node.fullWord ){
            list.add(word);
        }
        Iterator entries = node.map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            Character key = (Character) entry.getKey();

            TrieNode value = (TrieNode) entry.getValue();
            preOrderTraverse(value, list, word+key);

        }
    }

    public static void main(String[] args)
    {

        Trie tree = new Trie();

        String[] words = {"an", "ant", "all", "allot", "alloy", "aloe", "are", "ate", "be"};
        for (int i = 0; i < words.length; i++)
            tree.insertWord(tree.root, words[i]);

        ArrayList<String> tem = tree.getAllPrefixMatches("al");
        for (int i = 0; i < tem.size(); i++)
            System.out.println(tem.get(i));

    }


}
