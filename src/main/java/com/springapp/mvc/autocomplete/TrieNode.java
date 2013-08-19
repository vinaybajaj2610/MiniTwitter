package com.springapp.mvc.autocomplete;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: vinay
 * Date: 16/8/13
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */

public class TrieNode
{
    char letter;
    HashMap map;
    boolean fullWord;

    TrieNode(char letter, boolean fullWord)
    {
        this.letter = letter;
        map = new HashMap<Character,TrieNode>();
        this.fullWord = fullWord;
    }
}