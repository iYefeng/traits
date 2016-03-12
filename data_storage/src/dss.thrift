/* 
 * The first thing to know about are types. The available types in Thrift are:
 *
 *  bool        Boolean, one byte
 *  byte        Signed byte
 *  i16         Signed 16-bit integer
 *  i32         Signed 32-bit integer
 *  i64         Signed 64-bit integer
 *  double      64-bit floating point value
 *  string      String
 *  binary      Blob (byte array)
 *  map<t1,t2>  Map from one type to another
 *  list<t1>    Ordered list of one type
 *  set<t1>     Set of unique elements of one type
 *
 * Did you also notice that Thrift supports C style comments?
 */

namespace cpp dss
namespace d dss
namespace java dss
namespace php dss
namespace perl dss
namespace haxe dss

struct request {
  1: required string key,
  2: required string value,
  3: optional i32 expire = 0,
}

service DsServer {

  string ping(),
  
  string get(1:string key),
  list<string> getm(1:list<string> keys),
  
  void put(1:request req),
  void putm(1:list<request> reqs),
  
  string status()
}

