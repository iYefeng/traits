package algorithm;

import java.util.ArrayList;

/**
 * Created by yefeng on 16/11/17.
 *
 * 输入一棵二元查找树,将该二元查找树转换成一个排序的双向链表。 要求不能创建任何新的结点,只调整指针的指向。
 *    10
 *    /\
 *    6 14
 *    /\/\
 *    4 8 12 16
 *    转换成双向链表 4=6=8=10=12=14=16。
 */
public class Tree2LinkedList {

    static class BSTreeNode
    {
        public BSTreeNode(int value) { val_ = value; }
        int val_;            // value of node
        BSTreeNode left_ = null;    // left child of node
        BSTreeNode right_ = null;   // right child of node
    }

    static BSTreeNode head, tail;

    public static BSTreeNode buildTree(int[] data) {
        BSTreeNode root = null;
        BSTreeNode tmp = null;
        if (data.length == 0) return root;

        root = new BSTreeNode(data[0]);
        for (int i = 1; i < data.length; ++i) {
            tmp = root;
            while (true) {
                if (tmp.val_ > data[i]) {
                    if (tmp.left_ == null) {
                        tmp.left_ = new BSTreeNode(data[i]);
                        break;
                    } else {
                        tmp = tmp.left_;
                    }
                } else {
                    if (tmp.right_ == null) {
                        tmp.right_ = new BSTreeNode(data[i]);
                        break;
                    } else {
                        tmp = tmp.right_;
                    }
                }
            }
        }
        return root;
    }

    public static void printTree(BSTreeNode root) {
        if (root.left_ != null) printTree(root.left_);
        System.out.println(root.val_);
        if (root.right_ != null) printTree(root.right_);
    }

    public static void printLinkedListFromHead(BSTreeNode head) {

        while (head!= null) {
            System.out.println(head.val_);
            head = head.right_;
        }
    }

    public static void printLinkedListFromTail(BSTreeNode tail) {
        while (tail != null) {
            System.out.println(tail.val_);
            tail = tail.left_;
        }
    }


    public static void tree2linkedlist(BSTreeNode root) {

        if (root == null) {
            head = null;
            tail = null;
            return;
        }

        if(root.left_ != null) tree2linkedlist(root.left_);
        changeNode(root);
        if(root.right_ != null) tree2linkedlist(root.right_);
    }

    public static void changeNode(BSTreeNode node) {
        //初始时，双向链表中无节点，head及tail均为null
        if(head == null){
            head = node;
            tail = node;
        }else{
            //将新node的左指针指向当前tail,再将当前tail的右指针指向新node，最后将tail后移
            node.left_ = tail;
            tail.right_ = node;
            tail = node;
        }
    }

    public static void main(String[] args) {
        int[] data = {10, 6, 14, 4, 8, 12, 16};
        BSTreeNode root = buildTree(data);
        printTree(root);
        tree2linkedlist(root);
        System.out.println("-----------");
        printLinkedListFromHead(head);
        System.out.println("-----------");
        printLinkedListFromTail(tail);

    }
}
