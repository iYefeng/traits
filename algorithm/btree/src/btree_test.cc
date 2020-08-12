#include <iostream>
#include "btree.h"

using namespace std;

int main()
{

    BTree<int, int> btree(3);

    // test "insert"
    btree.insert(1, 1);
    btree.insert(3, 3);
    btree.insert(7, 7);
    btree.insert(10, 10);
    btree.insert(11, 11);
    btree.insert(13, 13);
    btree.insert(14, 14);
    btree.insert(15, 15);
    btree.insert(18, 18);
    btree.insert(16, 16);
    btree.insert(19, 19);
    btree.insert(24, 24);
    btree.insert(25, 25);
    btree.insert(26, 26);
    btree.insert(21, 21);
    btree.insert(4, 4);
    btree.insert(5, 5);
    btree.insert(20, 20);
    btree.insert(22, 22);
    btree.insert(2, 2);
    btree.insert(17, 17);
    btree.insert(12, 12);
    btree.insert(6, 6);
    btree.print();

    // test "find"
    cout << btree.find(0) << endl;
    cout << btree.find(1) << endl;
    cout << btree.find(20) << endl;

    // test "erase"
    btree.erase(6);
    btree.print();
    btree.erase(21);
    btree.print();
    btree.erase(20);
    btree.print();
    btree.erase(19);
    btree.print();
    btree.erase(22);
    btree.print();
    btree.erase(0);
    btree.erase(8);

    return 0;
}
