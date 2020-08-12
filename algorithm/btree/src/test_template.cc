#include<iostream>
#include "test_template.h"

using namespace std;

int main(){
    CompareDemo<int> cd;
    cout<<cd.compare(2,3)<<endl;
    return 0;
}
