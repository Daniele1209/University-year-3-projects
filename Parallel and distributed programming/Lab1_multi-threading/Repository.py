import random

class Repository:

    def __init__(self):
        self.logHistory = {}
        self.initialBalances = {}
        self.lastId = 0

    def appendLog(self, operation, amount, accountX, accountY):
        idGeneration = random.randint(1, 1000)
        while idGeneration in self.logHistory.keys():
            idGeneration = random.randint(1, 1000)
        self.lastId = idGeneration
        logString = str(operation.getOperationType()) + ' | Account 1: ' + str(accountX.getName()) + ' | Account 2: ' + str(accountY.getName()) + ' | Amount: ' + str(amount)
        self.logHistory[idGeneration] = logString
        
        return self.lastId, logString

    def appendInitialBalance(self, account):
        self.initialBalances[str(account.getName())] = account.getBalance()