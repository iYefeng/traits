import logging
import argparse

parser = argparse.ArgumentParser(description='python logger example')
parser.add_argument('-d', '--debug', action='store_true', dest='debug', help='Enable debug info')
args = parser.parse_args()

logfile = './log.txt'
logger = logging.getLogger()
hdlr = logging.FileHandler(logfile)
formatter = logging.Formatter('[%(levelname)s] (%(filename)s:%(lineno)d) %(asctime)s: '
        '%(message)s')
hdlr.setFormatter(formatter)
logger.addHandler(hdlr)
hdlr2 = logging.StreamHandler()
hdlr2.setFormatter(formatter)
logger.addHandler(hdlr2)

if args.debug:
    logger.setLevel(logging.INFO)
else:
    logger.setLevel(logging.ERROR)


logger.info('message')
