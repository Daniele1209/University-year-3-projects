from Models import SymbolTable
import re

TOKENS_PATH = '../Resources/token.in'

class LexicalAnalyser:

    def __init__(self, symbolTable):
        self.lexicalErrors = {}
        self.tokens = {'operators': [], 'separators': [], 'reserved': []}
        self.PIF = []
        self.ST = symbolTable
        self.regexTokenizer_string = ''
        self.loadTokens()

    def checkIdentifier(self, token):
        return self.patternMatch('^[a-zA-Z_$][a-zA-Z_$0-9]*$', token) and token is not None

    def patternMatch(self, pattern, token):
        pattern = re.compile(pattern)
        if pattern.match(token):
            return True

    def getTokensList(self):
        returnedList = []
        for tokenList in self.tokens.values():
            for token in tokenList:
                returnedList.append(token)
        return returnedList

    def checkConstant(self, token):
        # Integer constant case
        if self.patternMatch('^\d+$', token) and token is not None:
            return True
        # String constant case
        if self.patternMatch('".*?"', token) and token is not None:
            return True
        # Boolean constant case
        if self.patternMatch('True|False', token) and token is not None:
            return True

        return False

    def genPIF(self, token, position):
        self.PIF.append([token, position])

    def loadTokens(self):
        currentItem = ''
        with open(TOKENS_PATH) as file:
            tokens = file.read().splitlines()
            for token in tokens:
                if token == 'OPERATORS':
                    currentItem = 'operators'
                elif token == 'SEPARATORS':
                    currentItem = 'separators'
                elif token == 'RESERVED':
                    currentItem = 'reserved'
                else:
                    self.tokens[currentItem].append(token)

            file.close()
        print(self.tokens)

    def getTokens(self, line):
        idx = 0
        separatorList = ['\n', ' ', '\t']

        while idx < len(line):

            quoteClosedNumber = 0
            finalToken = ''
            while idx < len(line) and line[idx] in separatorList:
                idx += 1
            while idx < len(line) and (line[idx] not in separatorList or quoteClosedNumber % 2 != 0):
                finalToken += line[idx]
                if line[idx] == '"':
                    quoteClosedNumber += 1
                idx += 1

            print('FINAL TOKEN: ' + finalToken)
            if finalToken in self.getTokensList():
                self.genPIF(finalToken, 0)
            else:
                if self.checkIdentifier(finalToken):
                    symTableId = self.ST.pos(finalToken)
                    self.genPIF('identifier', symTableId)
                elif self.checkConstant(finalToken):
                    symTableId = self.ST.pos(finalToken)
                    self.genPIF('constant', symTableId)

                else:
                    raise Exception(finalToken)




    def scan(self, fileName):
        lineIndex = 0

        with open(fileName, 'r') as file:
            for line in file:
                lineIndex += 1
                line = line.strip()
                try:
                    if len(line) != 0:
                        print('LINE: ' + line)
                        self.getTokens(line)
                except Exception as err:
                    print('Lexical error at line: ' + str(lineIndex) + ' token: ' + str(err) + ' !')
                    break

        # Writing to ST file and PIF file
        with open('../Programs/PIF.out', 'w') as PIFfile:
            for item in self.PIF:
                PIFfile.write(str(item[0]) + ' ' + str(item[1]) + '\n')
        PIFfile.close()

        with open('../Programs/ST.out', 'w') as STfile:
            for item in self.ST.get_symTable().items():
                STfile.write(str(item[0]) + ' ' + str(item[1]) + '\n')
        STfile.close()

