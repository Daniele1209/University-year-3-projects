import time

class Producer:
    def __init__(self, array1, array2):
        self.array1 = array1
        self.array2 = array2

    def run(self, workQueue, finishQueue):
        finishQueue.put(False)
        for index in range(0, len(self.array1)):
            print('PRODUCER -> ' + str(self.array1[index]) + ' * ' + str(self.array2[index]))
            workQueue.put(self.array1[index] * self.array2[index])
        finishQueue.put(False)