from Models.LexicalAnalyser import LexicalAnalyser
from Models.SymbolTable import SymbolTable

ST = SymbolTable()
lexicalAnalyser = LexicalAnalyser(ST)
lexicalAnalyser.scan('../Programs/p1err.txt')