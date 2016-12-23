SHELL=/bin/bash

TARGET=bin/avl_test.exe

CC=gcc
CXX=g++ -std=c++11
LINK=g++ -std=c++11
AR=ar

INC=./
LIBS=
LDFLAGS=-L./
ARFLAGS=
CCFLAGS=-Wall -I$(INC)
CXXFLAGS = $(CCFLAGS) -Dc_plusplus

.PHONY: all clean

all: $(TARGET)

$(TARGET): src/avl_test.o src/avl_tree.o
	$(CXX) $^ ${LIBS} -o $@

%.o:%.c
	$(call print_status, "compile",$@)
	$(CC) -c $(CCFLAGS) $< -o $@

%.o:%.cc
	$(call print_status, "compile",$@)
	$(CXX) -c $(CXXFLAGS) $< -o $@

%.o:%.cpp
	$(call print_status, "compile",$@)
	$(CXX) -c $(CXXFLAGS)  $< -o $@

clean:
	rm $(TARGET) src/*.o
