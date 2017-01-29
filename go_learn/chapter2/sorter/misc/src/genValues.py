#!/usr/bin/env python
# -*- coding: utf-8 -*-

import random
import sys
from optparse import OptionParser
import logging

logging.basicConfig(level=logging.DEBUG,
        format='%(asctime)s %(filename)s[line:%(lineno)d] %(levelname)s %(message)s',
        datefmt='%a, %d %b %Y %H:%M:%S')
        #filename='myapp.log',
        #filemode='w')

MSG_USAGE = "genValues -n <number> -o <filename> -s <start> -e <end>"
opt = OptionParser(MSG_USAGE)

def genInts(num, start, end, filename):
    with open(filename, 'w') as ofile:
        for i in range(num):
            print >> ofile, random.randint(start, end)

def getUserParas():
    try:
        opt.add_option(
                '-o',
                '--outfile',
                dest='outfile',
                type=str,
                help='dest file for write random values')
        opt.add_option(
                '-s',
                '--start',
                dest='start',
                type=int,
                help='start value')
        opt.add_option(
                '-e',
                '--end',
                dest='end',
                type=int,
                help='end value')
        opt.add_option(
                '-n',
                '--num',
                dest='num',
                type=int,
                help='the number of values to generate')
        (options, args) = opt.parse_args()
        return (options, args)
    except Exception, ex:
        print("exception :{0}".format(str(ex)))
        return None

def main():
    options, args = getUserParas()
    if options is None:
        logging.debug("userParas is None")
        sys.exit(0)
    if options.num and options.outfile and options.start is not None and options.end is not None:
        genInts(options.num, options.start, options.end, options.outfile)
    else:
        logging.debug("{}, {}, {}, {}".format(options.num, options.start, options.end, options.outfile))
        opt.print_help()

if __name__ == "__main__":
    logging.info("start working")
    main()
