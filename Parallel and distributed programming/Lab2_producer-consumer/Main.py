import queue
import time
from queue import Queue
from Consumer import Consumer
from Producer import Producer
from threading import Thread

if __name__ == '__main__':
    workQueue = Queue()
    finishQueue = Queue()
    array1 = [1, 2, 3, 4]
    array2 = [5, 6, 7, 8]

    producerObj = Producer(array1, array2)
    consumerObj = Consumer(len(array1))

    producer = Thread(target = producerObj.run, args = [workQueue, finishQueue], daemon = True)
    consumer = Thread(target = consumerObj.run, args = [workQueue, finishQueue], daemon = True)

    producer.start()
    consumer.start()

    producer.join()
    print('Producer finished !')

    consumer.join()
    print('Consumer finished !')

    print('Main thread has finished !')