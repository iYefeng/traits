#!/usr/bin/env python
# -*- coding: utf-8 -*-
import random
import hashlib

def trailing_zeros(num):
  if num == 0:
    return 64
  p = 0
  while (num >> p) & 1 == 0:
    p += 1
  return p

def estimate_cardinality(values, k):
  num_buckets = 2 ** k
  max_zeros = [0] * num_buckets
  for value in values:
    h = hash(value)
    bucket = h & (num_buckets - 1)
    bucket_hash = h >> k
    max_zeros[bucket] = max(max_zeros[bucket], trailing_zeros(bucket_hash))
  return 2 ** (float(sum(max_zeros)) / num_buckets) * num_buckets * 0.79402

if __name__ == "__main__":
  values = [hash(hashlib.md5(str(random.randint(1, 80000))).hexdigest()) for i in range(580000)]
  print estimate_cardinality(values, 10)

