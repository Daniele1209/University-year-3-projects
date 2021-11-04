import random

class Repository:

    def __init__(self):
        self.logHistory = {}
        self.initialBalances = {}
<<<<<<< HEAD
        self.lastId = 0

    def appendLog(self, operation, amount, accountX, accountY):
        idGeneration = random.randint(1, 1000)
        while idGeneration in self.logHistory.keys():
            idGeneration = random.randint(1, 1000)
        self.lastId = idGeneration
        logString = str(operation.getOperationType()) + ' | Account 1: ' + str(accountX.getName()) + ' | Account 2: ' + str(accountY.getName()) + ' | Amount: ' + str(amount)
        self.logHistory[idGeneration] = logString
        
        return self.lastId, logString
=======
        self.log_filename = 'log_file.txt'
        self.accounts = []
        self.lastId = -1
        self.cleanLog()

    def appendLog(self, logString, id):
        self.logHistory[id] = logString
        with open(self.log_filename, 'a') as file:
            file.write(logString)
            file.write('\n')

        return logString

    def cleanLog(self):
        open(self.log_filename, 'w').close()
    
    def appendAccount(self, account):
        self.accounts.append(account)
    
    def getAccounts(self):
        return self.accounts

    def getInitialBalance(self, account):
        return self.initialBalances[str(account.getName())]
>>>>>>> main

    def appendInitialBalance(self, account):
        self.initialBalances[str(account.getName())] = account.getBalance()

    def generateId(self):
        idGeneration = self.lastId + 1
        self.lastId += 1

        return idGeneration