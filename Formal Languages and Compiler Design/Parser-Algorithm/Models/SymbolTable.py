class SymbolTable:

    def __init__(self):
        self.symTable = {}

    def get_symTable(self):
        return self.symTable

    def get_string_sum(self, token):
        ascii_sum = 0

        for item in token:
            ascii_sum += ord(item)

        return ascii_sum

    def hash_key(self, token, m):
        ascii_sum = self.get_string_sum(token)
        
        if m == -1:
            if isinstance(ascii_sum, int):
                return ascii_sum % 256
            else:
                return int(token) % 256
        else:
            if isinstance(ascii_sum, int):
                return ascii_sum % 256 + m
            else:
                return int(token) % 256 + m

    def pos(self, token):

        if token not in self.symTable.values():
            position = self.hash_key(token, -1)
            while position in self.symTable:
                position = self.hash_key(token, 99)
            self.symTable[position] = token
            return position

        else:
            if isinstance(token, int):
                position = token % 256
            else:
                position = self.get_string_sum(token) % 256
                
            if self.symTable[position] == token:
                return position
            else:
                while self.symTable[position] != token:
                    position += 99
                return position

if __name__ == "__main__":
    ST = SymbolTable()
    print(ST.pos("a"))
    print(ST.pos("2"))
    print(ST.pos("b"))
    print(ST.pos("2"))
    print(ST.pos("abcdf"))
    print(ST.pos("456"))
    print(ST.pos("a"))

    print(ST.get_symTable())

