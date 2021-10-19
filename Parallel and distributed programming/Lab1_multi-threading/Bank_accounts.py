import logging 
import threading 
from threading import Lock
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

repository = Repository()
TRANSACTION_NUMBER = 10
THREAD_NUMBER = 100
ACCOUNT_NUMBER = 40
CONSISTENCY_CHECK = 10
consistency_mutex = Lock()
failed_passes = 0

def executeConsistencyCheck():
    consistency_mutex.acquire()
    print('Started consistency check !')
    # Make a list of booleans to keep count of the consistency
    consistencyList = {}
    accountsList = repository.getAccounts()
    # Iterate through all the accouns
    for account in accountsList:
        initialAccountBalance = repository.getInitialBalance(account)
        currentAccountBalance = account.getBalance()
        accountLogs = account.getLogs()
        # Go through all of the account logs and process the operations to see if we end up with the current balance
        for log in accountLogs.values():
            log_split = log.split(',')
            if log_split == 'send':
                initialAccountBalance -= int(log_split[1].split(':')[1])
            else:
                initialAccountBalance += int(log_split[1].split(':')[1])
            if initialAccountBalance == currentAccountBalance:
                consistencyList[account.getName()] = True
    if False not in consistencyList.values():
        print('Consistency check passed !')
    else:
        print('Consistency check failed !')
        failed_passes += 1
    consistency_mutex.release()


def executeAddOperation(operation, accountX, accountY):
    amount = operation.getAmount()
    generatedId = repository.generateId()
    accountY.addBalance(amount, accountX, generatedId)
    transactionLog = repository.appendLog(accountX.getLog(generatedId), generatedId)
    print(transactionLog)

def executeSubtractionOperation(operation, accountX, accountY):
    amount = operation.getAmount()
    generatedId = repository.generateId()
    accountX.subtractBalance(amount, accountY, generatedId)
    transactionLog = repository.appendLog(accountX.getLog(generatedId), generatedId)
    print(transactionLog)

def threadTransaction(operation, accountX, accountY):
    try:
        amount = operation.getAmount()
        generatedId = repository.generateId()
        accountX.subtractBalance(amount, accountY, generatedId)
        transactionLog = repository.appendLog(accountX.getLog(generatedId), generatedId)
        print(transactionLog)
        accountY.addBalance(amount, accountX, generatedId)
        transactionLog = repository.appendLog(accountY.getLog(generatedId), generatedId)
        print(transactionLog)
    except Exception:
        print('Balance not sufficient !')

def threadFunctionLoop(nb_of_loops):
    for idx in range(nb_of_loops):
        transferOperation = Operation('add', random.randint(1, 300))
        accountX, accountY = random.sample(set(repository.getAccounts()), 2)
        threadTransaction(transferOperation, accountX, accountY)

if __name__ == '__main__':

    # Declare the user accounts
    # Append accounts to list in order to make transactions between them at random
    # Adding initial balance to repo in order to keep track of initial balance
    for account_idx in range(1, ACCOUNT_NUMBER):
        new_account = Account('Name ' + str(account_idx))
        repository.appendAccount(new_account)
        repository.appendInitialBalance(new_account)
    
    print('Accounts generated successfully!')

    start = time.time()

    for threadIndex in range(THREAD_NUMBER):

        if threadIndex % CONSISTENCY_CHECK == 0:
            executeConsistencyCheck()
        else:
            #transferOperation = Operation('add', random.randint(1, 300))
            #accountX, accountY = random.sample(set(repository.getAccounts()), 2)
            # Start a thread for a transaction between the 2 random selected accounts
            #t = threading.Thread(target = threadTransaction(transferOperation, accountX, accountY), args=(threadIndex, ))
            t = threading.Thread(target = threadFunctionLoop(TRANSACTION_NUMBER), args=(threadIndex, ))
            t.start()
    t.join()
    elapsed_time = time.time() - start
    print('Elapsed time: ' + str(elapsed_time))
    print('Failed consistency checks: ' + str(failed_passes))