from os import error
import random
from threading import Lock

class Account:

    def __init__(self, accountName):
        self.accountName = accountName
        self.log = {}
        self.accountBalance = random.randint(1000, 5000)
        self.balance_mutex = Lock()
        self.log_mutex = Lock()
        print('Account ' + self.accountName + ' has been generated with ' + str(self.accountBalance) + ' available !')

    def getName(self):
        return self.accountName

    def getLog(self, id):
        return self.log[id]
    
    def getLogs(self):
        return self.log
    
    def chackBalance(self, amount):
        if self.accountBalance - amount < 0.0:
            return False
        return True

    def addBalance(self, amount, payer, id):
        self.balance_mutex.acquire()
        self.accountBalance += amount
        self.balance_mutex.release()
        logString = 'receive' + ',Amount:' + str(amount) + ',Account1:' + str(payer.getName()) + ',Account2:' + str(self.getName())

        self.log_mutex.acquire()
        self.log[id] = logString
        self.log_mutex.release()
        

    def getBalance(self):
        return self.accountBalance

    def subtractBalance(self, amount, recipient, id):
        if self.chackBalance(amount):
            self.balance_mutex.acquire()
            self.accountBalance -= amount
            self.balance_mutex.release()
            logString = 'send' + ',Amount:' + str(amount) + ',Account1:' + str(self.getName()) + ',Account2:' + str(recipient.getName())

            self.log_mutex.acquire()
            self.log[id] = logString
            self.log_mutex.release()
        else:
            raise Exception()

    def appendLog(self, transactionId, logString):
        self.log[int(transactionId)] = logString
        f = open("logfile.txt", "a")
        f.write(str(transactionId) + " " + logString)
        f.close()
