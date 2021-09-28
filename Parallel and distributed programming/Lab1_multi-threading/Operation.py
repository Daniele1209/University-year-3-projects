class Operation:

    def __init__(self, operationType, amount):
        self.operationType = operationType
        self.amount = amount

    def getLog(self):
        return 'Operation of type: ' + str(self.operationType) + ' has been executed with: ' + str(self.amount) + ' amount !'

    def getOperationType(self):
        return self.operationType

    def getAmount(self):
        return self.amount

