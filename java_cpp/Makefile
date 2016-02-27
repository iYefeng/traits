
TARGET1=bin/test.exe
TARGET2=bin/libSample1.so

#SRCS=$(filter %.c, $(shell ls src/*))
#OBJS=$(patsubst %.c, %.o, $(SRCS))

CC=gcc
CXX=g++

INC=/usr/lib/jvm/jdk1.7.0_80/include -I/usr/lib/jvm/jdk1.7.0_80/include/linux
LIBS=
LDFLAGS=-L./
ARFLAGS=
CFLAGS=-Wall -I$(INC)
CXXFLAGS=

.PHONY: all clean protoc

all:$(TARGET1) $(TARGET2)

$(TARGET1): src/test.o
	@echo "build " $@
	$(CC) -o  $@ $^

$(TARGET2): src/com_traits_Sample1.o
	@echo "build " $@
	$(CC) -o $@ $^ -fPIC -shared

%.o:%.c
	@echo "compile " $@ 
	$(CC) -c $(CFLAGS) $(CPPFLAGS)  $< -o $@

%.o:%.cc
	@echo "complie " $@ 
	$(CXX) -c $(CPPFLAGS) $< -o $@

%.o:%.cpp
	@echo "compile " $@
	$(CXX) -c $(CPPFLAGS)  $< -o $@



protoc:
	protoc --cpp_out=./ --java_out=src/javacpp/src/main/java --python_out=src/python src/data_trans.proto

clean:
	rm $(TARGET1) $(TARGET2) src/test.o src/com_traits_Sample1.o
