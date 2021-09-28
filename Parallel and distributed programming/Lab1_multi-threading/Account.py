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

    def subtractBalance(self, amount):
        if self.chackBalance(amount):
            self.accountBalance -= amount
        else:
            print('Balance not sufficient !')

    def getBalance(self):
        return self.accountBalance