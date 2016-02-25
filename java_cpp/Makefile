
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

.PHONY: all clean

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

clean:
	rm $(TARGET) $(OBJS)
