import time

class Consumer:
    def __init__(self, operationCount):
        self.finalSum = 0
        self.operationCount = operationCount

    def run(self, workQueue, finishQueue):
        for currentOp in range(0, self.operationCount):
            if not workQueue.empty():
                resultProduct = workQueue.get()
                self.finalSum += resultProduct
                print('CONSUMER -> product at position: ' + str(currentOp) + ' = ' + str(resultProduct))
                print('CONSUMER -> Sum = ' + str(self.finalSum))
            else:
                q = finishQueue.get()
                if q == True:
                    break