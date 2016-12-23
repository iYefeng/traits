/************************************************************
 * Use of this source code is governed by a BSD-style license
 * that can be found in the License file.
 * 
 * Author        : yefeng
 * Email         : yefeng38083120@126.com
 * Create time   : 2016-12-04 17:23
 * Last modified : 2016-12-04 17:23
 * Filename      : demo1.c
 * Description   : 
 * *********************************************************/

extern int input, result;

void test(void)
{
  input = 1;
  __asm__ __volatile__ ("movl %1,%0" : "=r" (result) : "r" (input)); 
  return;
}
