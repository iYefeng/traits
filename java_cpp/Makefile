
TARGET=bin/test

SRCS=$(filter %.c, $(shell ls src/*))
OBJS=$(patsubst %.c, %.o, $(SRCS))

CC=gcc
CXX=g++

INC=./
LIBS=
LDFLAGS=-L./
ARFLAGS=
CFLAGS=-Wall -I$(INC)
CXXFLAGS=

.PHONY: all clean protoc

all:$(TARGET)

$(TARGET):$(OBJS)
	@echo "link " $@
	$(CC) -o  $@ $^

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
	rm $(TARGET) $(OBJS)
