from Models.FiniteAutomata import FiniteAutomata

def printMenu(commands):
    print('== Commands ==')
    for comm in commands.keys():
        print('>> ' + comm)
    print('>> check dfa')
    print('>> accepted')
    print('>> exit')


def checkDfa():
    if FA.checkDFA():
        print('FA is DFA')
    else:
        print('FA is not DFA')


def isAccepted():
    input_seq = input('enter sequence: ')
    if FA.acceptedSequence(input_seq):
        print('sequence accepted')
    else:
        print('sequence not accepted')

FA = FiniteAutomata()
FA.read('../Programs/FA.in')
items = FA.getData()
commands = {'states': items['states'], 'alphabet': items['alphabet'],
                'transitions': items['transitions'], 'final states': items['final states']}

if __name__ == '__main__':
    printMenu(commands)

    command = input('>> ')
    while command != 'exit':
        if command == 'check dfa':
            checkDfa()
        elif command == 'accepted':
            isAccepted()
        elif command in commands.keys():
            print(commands[command])
        else:
            print('Command not valid !')
        command = input('>> ')
