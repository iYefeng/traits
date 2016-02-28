SHELL=/bin/bash

TARGET1=bin/test.exe
TARGET2=bin/libSample1.so
TARGET3=bin/json_test.exe

#SRCS=$(filter %.c, $(shell ls src/*))
#OBJS=$(patsubst %.c, %.o, $(SRCS))

CC=gcc
CXX=g++

INC=/usr/lib/jvm/jdk1.7.0_80/include -I/usr/lib/jvm/jdk1.7.0_80/include/linux -I./thirdparty/rapidjson/include
LIBS=
LDFLAGS=-L./
ARFLAGS=
CFLAGS=-Wall -I$(INC)
CXXFLAGS=

BLACK 	= "\e[30;1m"
RED  		= "\e[31;1m"
GREEN 	= "\e[32;1m"
YELLOW 	= "\e[33;1m"
BLUE  	= "\e[34;1m"
PURPLE 	= "\e[35;1m"
CYAN  	= "\e[36;1m"
WHITE 	= "\e[37;1m"
CLEAR		= "\e[0m"

DATA = "hello"

define print_status
	@echo -e $(BLUE)  $(1) $(YELLOW)"["$(2)"]"$(CLEAR)
endef

.PHONY: all clean protoc

all:$(TARGET1) $(TARGET2) $(TARGET3)

$(TARGET1): src/test.o
	$(call print_status, "build",$@)
	$(CC) -o  $@ $^

$(TARGET2): src/com_traits_Sample1.o
	$(call print_status, "build",$@)
	$(CC) -o $@ $^ -fPIC -shared

$(TARGET3): src/json_test.o
	$(call print_status, "build",$@)
	$(CXX) -o $@ $^

%.o:%.c
	$(call print_status, "compile",$@)
	$(CC) -c $(CFLAGS) $< -o $@

%.o:%.cc
	$(call print_status, "compile",$@)
	$(CXX) -c $(CFLAGS) $(CXXFLAGS) $< -o $@

%.o:%.cpp
	$(call print_status, "compile",$@)
	$(CXX) -c $(CFLAGS) $(CXXFLAGS)  $< -o $@

protoc:
	protoc --cpp_out=./ --java_out=src/javacpp/src/main/java --python_out=src/python src/data_trans.proto

clean:
	rm $(TARGET1) $(TARGET2) $(TARGET3) src/test.o src/com_traits_Sample1.o src/json_test.o
