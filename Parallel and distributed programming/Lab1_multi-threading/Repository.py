import random

class Repository:

    def __init__(self):
        self.logHistory = {}
        self.initialBalances = {}
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

    def appendInitialBalance(self, account):
        self.initialBalances[str(account.getName())] = account.getBalance()

    def generateId(self):
        idGeneration = self.lastId + 1
        self.lastId += 1

        return idGeneration