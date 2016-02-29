#include <stdio.h>
#include <string.h>
#include <locale.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
  int len;
  int i;
  char *str = "你好abc";
  wchar_t tmp[256] = {'\0'};
  setlocale(LC_ALL, "");
  len = mbstowcs(tmp, str, strlen(str));
  printf("%d\n", (int)strlen(str));
  printf("%d\n", (int)wcslen(tmp));
  printf("%d\n", len);
  for (i = 0; i < len; ++i) {
    printf("%ld\n", (long)tmp[i]);
  }

  return 0;
}
