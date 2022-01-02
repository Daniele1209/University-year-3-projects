# closure primeste o multime
# se uita daca am punct in fata unui non-terminal
from copy import deepcopy
'''
LR Item [A -> alpha.beta] is a tuple (A, alpha, ., beta)
'''


class Parser:
    def __init__(self, grammar):
        self.grammar = grammar  # enriched grammar

    def closure(self, itemCollection):
        C = itemCollection  # set
        finished = False
        while not finished:  # while C does modify
            finished = True
            for item in itemCollection:
                alpha = item[1]  # prefix
                beta = item[3]
                if len(beta) == 0:
                    continue
                B = beta.split()[0]
                if B in self.grammar.terminals:
                    continue
                for prod in self.grammar.get_productions_for_nonTerminal(B):
                    theItem = (B, '', '.', prod)
                    if theItem not in C:
                        C.append(theItem)
                        finished = False
        return C

    def goto(self, state, X):
        stateSet = set()
        for item in state:
            if item[3].split()[0] == X:  # if beta contains X on the first pos
                newItem = (item[0], item[1] + ' ' + X, '.', item[3][2:])   # remove X from beta
                stateSet.add(newItem)
        return self.closure(stateSet)

    def CanonicalCollection(self):
        C = set()
        s0 = self.closure({('S^', '', '.', 'S')})  # enriched grammar S^
        C.add(s0)
        finished = False
        while not finished:
            finished = True
            for state in C:
                for X in self.grammar.terminals + self.grammar.nonTerminals:
                    nextState = self.goto(state, X)
                    if len(nextState) and nextState not in C:
                        finished = False
                        C.add(nextState)
        return C