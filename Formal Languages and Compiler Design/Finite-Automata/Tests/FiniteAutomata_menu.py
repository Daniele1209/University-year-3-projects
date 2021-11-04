from Models.FiniteAutomata import FiniteAutomata

def printMenu(commands):
    print('== Commands ==')
    for comm in commands.keys():
        print('>> ' + comm)
    print('>> exit')

if __name__ == '__main__':
    FA = FiniteAutomata()
    FA.read('../Programs/FA.in')
    items = FA.getData()

    commands = {'states': items['states'], 'alphabet': items['alphabet'], 'transitions': items['transitions'], 'final states': items['final states']}
    printMenu(commands)

    command = input('>> ')
    while command != 'exit':
        if command in commands.keys():
            print(commands[command])
        else:
            print('Command not valid !')
        command = input('>> ')
