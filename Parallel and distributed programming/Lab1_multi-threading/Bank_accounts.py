import logging 
import threading 
import time
from Account import Account
from Operation import Operation
from Repository import Repository
import random

"""
At a bank, we have to keep track of the balance of some accounts. 
Also, each account has an associated log (the list of records of operations performed on that account). 
Each operation record shall have a unique serial number, that is incremented for each operation performed in the bank.

We have concurrently run transfer operations, to be executer on multiple threads. 
Each operation transfers a given amount of money from one account to someother account, and also appends the information about the transfer to the logs of both accounts.

From time to time, as well as at the end of the program, a consistency check shall be executed.
 It shall verify that the amount of money in each account corresponds with the operations records associated to that account, and also that all operations
  on each account appear also in the logs of the source or destination of the transfer.
"""

def executeConsistencyCheck():
    pass

def executeOperation(operation, accountX, accountY):
    time.sleep(2)
    

if __name__ == '__main__':
    repository = Repository()
    accountList = []

    account1 = Account('Name 1')
    account2 = Account('Name 2')
    account3 = Account('Name 3')

    accountList.append(account1)
    accountList.append(account2)
    accountList.append(account3)

    repository.appendInitialBalance(account1)
    repository.appendInitialBalance(account2)
    repository.appendInitialBalance(account3)

    numberOfOperations = 10
    consistencyCheck = 3

    for operationIndex in range(numberOfOperations):
        
        if operationIndex % consistencyCheck == 0:
            executeConsistencyCheck()
        else:
            transferOperation = Operation('transfer', random.randint(1, 300))
            subtractOpreation = Operation('transfer', transferOperation.getAmount())
            accountX, accountY = random.sample(set(accountList), 2)
            x = threading.Thread(target = executeOperation(transferOperation, accountX, accountY), args=(1, ))
            x.start
)
            
