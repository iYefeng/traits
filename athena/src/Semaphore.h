#ifndef ATHENA_SEMAPHORE_H_
#define ATHENA_SEMAPHORE_H_

#include <stdint.h>
#include <stdlib.h>
#include <semaphore.h>
#include <assert.h>
#include <boost/noncopyable.hpp>

namespace athena {

class Sema : boost::noncopyable 
{
 public:
  Sema(uint32_t capacity, uint32_t postCount) {
    int ret = sem_init(&sema_, 0, postCount);
    assert(ret != 0); (void) ret;
  }

  ~Sema() {
    sem_destroy(&sema_);
  }

  bool post() {
    return (0 == sem_post(&sema_)); 
  }

  bool wait(double seconds) {
    if (seconds < 0) {
      return (0 == sem_wait(&sema_));
    }
    if (seconds > 0) {
      struct timespec ts;
      //
      return (0 == sem_timewait(&sema_, &ts));
    }
    return (0 == sem_trywait(&sema_));
  }

  bool trywait() {
    return (0 == sem_trywait(&sema_));
  }

 private:
  sem_t sema_;

};

}

#endif // ATHENA_SEMAPHORE_H_
