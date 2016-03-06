SHELL=/bin/bash

TARGET1=bin/test.exe
TARGET2=bin/tfidf_main.exe

#SRCS=$(filter %.cc, $(shell ls src/*))
#OBJS=$(patsubst %.cc, %.o, $(SRCS))

CC=gcc
CXX=g++

INC=./thirdparty/friso-1.6.0/include
LIBS=./thirdparty/friso-1.6.0/lib/libfriso.a \
		 -lm
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

define print_status
	@echo -e $(BLUE)  $(1) $(YELLOW)"["$(2)"]"$(CLEAR)
endef

.PHONY: all clean compile_thirdparty

all: $(TARGET1) ${TARGET2}

$(TARGET1): src/tst-friso.o
	$(call print_status, "build",$@)
	$(CXX) -static $^ ${LIBS} -o $@

${TARGET2}: src/tfidf_main.o
	$(call print_status, "build",$@)
	$(CXX) -static $^ ${LIBS} -o $@

src/tfidf_main.o: src/tfidf_main.cc src/tfidf.h
	$(call print_status, "compile",$@)
	$(CC) -c $(CFLAGS) $< -o $@

%.o:%.c
	$(call print_status, "compile",$@)
	$(CC) -c $(CFLAGS) $< -o $@

%.o:%.cc
	$(call print_status, "compile",$@)
	$(CXX) -c $(CFLAGS) $(CXXFLAGS) $< -o $@

%.o:%.cpp
	$(call print_status, "compile",$@)
	$(CXX) -c $(CFLAGS) $(CXXFLAGS)  $< -o $@

compile_thirdparty:
	cd thirdparty; unzip friso-1.6.0-src-lib-dict.zip
	cd thirdparty/friso-1.6.0/src; make static;
	mkdir -p thirdparty/friso-1.6.0/include
	cp thirdparty/friso-1.6.0/src/*.h thirdparty/friso-1.6.0/include
	cp thirdparty/friso-1.6.0/src/libfriso.a thirdparty/friso-1.6.0/lib
	cp -r thirdparty/friso-1.6.0/dict ./
	mkdir -p conf
	cp -r thirdparty/friso-1.6.0/friso.ini conf/
	cd thirdparty/friso-1.6.0/src; make clean;

clean:
	rm $(TARGET1)  src/*.o
