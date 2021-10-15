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
    

def executeAddOperation(operation, accountX, accountY):
    amount = operation.getAmount()
    accountX.subtractBalance(amount)
    transactionId, transactionLog = repository.appendLog(operation, amount, accountX, accountY)
    accountX.appendLog(transactionId, transactionLog)
    print(transactionLog)

def executeSubtractionOperation(operation, accountX, accountY):
    amount = operation.getAmount()
    accountX.addBalance(amount)
    transactionId, transactionLog = repository.appendLog(operation, amount, accountX, accountY)
    accountX.appendLog(transactionId, transactionLog)
    print(transactionLog)

if __name__ == '__main__':
    repository = Repository()
    accountList = []

    # Declare the user accounts
    account1 = Account('Name 1')
    account2 = Account('Name 2')
    account3 = Account('Name 3')

    # Append accounts to list in order to make transactions between them at random
    accountList.append(account1)
    accountList.append(account2)
    accountList.append(account3)

    # Adding initial balance to repo in order to keep track of initial balance
    repository.appendInitialBalance(account1)
    repository.appendInitialBalance(account2)
    repository.appendInitialBalance(account3)

    # Declare the number of total transactions and the step at which the consistency check will take place
    numberOfOperations = 10
    consistencyCheck = 3

    for operationIndex in range(numberOfOperations):

        if operationIndex % consistencyCheck == 0:
            executeConsistencyCheck()
        else:
            transferOperation = Operation('add', random.randint(1, 300))
            subtractOpreation = Operation('subtract', transferOperation.getAmount())
            accountX, accountY = random.sample(set(accountList), 2)
            # Start a thread for each of the 2 transactions
            if transferOperation.getOperationType() == 'add':
                t1 = threading.Thread(target = executeAddOperation(transferOperation, accountX, accountY), args=(1, ))
                t2 = threading.Thread(target = executeSubtractionOperation(subtractOpreation, accountY, accountX), args=(1, ))
                t1.start()
                t2.start()
                t1.join()
                t2.join()
            
    print('Account 1 final balance: ' + str(account1.getBalance()))
    print('Account 2 final balance: ' + str(account2.getBalance()))
    print('Account 3 final balance: ' + str(account3.getBalance()))