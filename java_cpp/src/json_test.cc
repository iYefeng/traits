#include <stdio.h>
#include <string>
#include <iostream>

#include "rapidjson/document.h"
#include "rapidjson/writer.h"
#include "rapidjson/stringbuffer.h"

using namespace rapidjson;

int main(int argc, char *argv[])
{
  // 1. Parse a json string into DOM
  const char* json = "{\"project\":\"rapidjson\",\"stars\":10}";
  Document d;
  d.Parse(json);
  
  // 2. Modify it by DOM
  Value& s = d["stars"];
  s.SetInt(s.GetInt() + 10);
  
  // 3. Stringify the DOM
  StringBuffer buffer;
  Writer<StringBuffer> writer(buffer);
  d.Accept(writer);
  
  // 4. Output json string
  std::cout << buffer.GetString() << std::endl;

  return 0;
}
