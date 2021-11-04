class FiniteAutomata:
    def __init__(self):
        Q = []      # finite set of states
        E = []      # finite alphabet
        D = {}      # transition function
        q0 = ''     # initial state
        F = []      # set of final states
        self.clear_values()

    def clear_values(self):
        self.Q = []
        self.E = []
        self.D = {}
        self.q0 = ''
        self.F = []

    def read(self, file_name):
        with open(file_name) as file:
            line = file.readline().strip()
            while line != '':
                if line == 'Q':
                    for state in file.readline().strip().split(' '):
                        self.Q.append(state)
                if line == 'E':
                    for state in file.readline().strip().split(' '):
                        self.E.append(state)
                if line == 'D':
                    line = file.readline()
                    while line[0] == '(':
                        trans_from_a, tras_from_b = line.strip().split('=')[0].replace('(', '').replace(')', '').split(',')
                        trans_to = line.strip().split('=')[1]
                        if (trans_from_a, tras_from_b) not in self.D.keys():
                            self.D[(trans_from_a, tras_from_b)] = trans_to
                        else:
                            self.D[(trans_from_a, tras_from_b)].append(trans_to)
                        line = file.readline()
                if line == 'q0':
                    self.q0 = file.readline()
                if line == 'F':
                    for state in file.readline().strip().strip(' '):
                        self.F.append(state)
                line = file.readline().strip()

    def checkDFA(self):
        for trans in self.D.values():
            if len(trans) >= 2:
                return False

        return True

    def acceptedSequence(self, input_sequence):
        if self.checkDFA():
            state = self.q0
            for s in input_sequence:
                if (state, s) in self.D.keys():
                    state = self.D[(state, s)][0]
                else:
                    return False
            if state in self.F:
                return True
        else:
            return False

    def getData(self):
        items = {}
        items['states'] = self.Q
        items['alphabet'] = self.E
        items['transitions'] = self.D
        items['final states'] = self.F
        return items
