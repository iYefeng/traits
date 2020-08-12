#include <iostream>
#include <memory>

using namespace std;

int main(int argc, char *argv[]) {
  std::unique_ptr<string> ps1, ps2;
  ps1 = unique_ptr<string>(new string("hello"));
  ps2 = std::move(ps1);
  ps1 = unique_ptr<string>(new string("alexia"));
  cout << *ps2 << *ps1 << endl;
  
  return 0;
}


