#!/bin/bash

#autoscan
aclocal \
  & autoconf \
  & autoheader \
  & automake --add-missing
