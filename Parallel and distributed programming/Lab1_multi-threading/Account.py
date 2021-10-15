import random

class Account:

    def __init__(self, accountName):
        self.accountName = accountName
        self.log = {}
        self.accountBalance = random.randint(1000, 5000)
        print('Account ' + self.accountName + ' has been generated with ' + str(self.accountBalance) + ' available !')

    def getName(self):
        return self.accountName
    
    def chackBalance(self, amount):
        if self.accountBalance - amount < 0.0:
            return False
        return True

    def addBalance(self, amount):
        self.accountBalance += amount

    def getBalance(self):
        return self.accountBalance

    def subtractBalance(self, amount):
        if self.chackBalance(amount):
            self.accountBalance -= amount
            return True
        else:
            print('Balance not sufficient !')
            return False

    def appendLog(self, transactionId, logString):
        self.log[int(transactionId)] = logString
        f = open("logfile.txt", "a")
        f.write(str(transactionId) + " " + logString)
        f.close()
