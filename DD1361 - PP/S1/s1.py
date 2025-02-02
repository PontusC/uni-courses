# Author: Pontus Curtsson pontuscu@kth.se

def dna():          # uppgift 1
    return "^[ACGT]+$"

def sorted():       # uppgift 2
    return "^9*8*7*6*5*4*3*2*1*0*$"

def hidden1(x):     # uppgift 3
    return "^.*("+ x +").*$"

def hidden2(x):     # uppgift 4
    expression = ""
    # creates a string consisting of the correct order of each char and its regex
    for char in list(x):
        expression += "(" + char + ").*"
    return "^.*" + expression + "$"

def equation():     # uppgift 5
    # there may be a + or - at the beginning, after that has to be at least 1 digit.
    # then there may be a +-*/ followed by at least 1 digit multiple times.
    # then there may be = possibly followed by + or -, and then at least 1 digit.
    # then there may be a operator followed by at least 1 digit multiple times.
    return "^[\+\-]?[0-9]+([\+\-\*\/][0-9]+)*([\=][\+\-]?[0-9]+(([\+\-\*\/][0-9]+)*))?$"

def parentheses():  # uppgift 6
    # ([\(](...)*[\)]) repeated with itself.
    return "^([\(](([\(](([\(](([\(](([\(][\)]))*[\)]))*[\)]))*[\)]))*[\)])+$"

def sorted3():      # uppgift 7
    return "^[0-9]*(01[2-9]|[0-1]2[3-9]|[0-2]3[4-9]|[0-3]4[5-9]|[0-4]5[6-9]|[0-5]6[7-9]|[0-6]7[8-9]|[0-7]89)[0-9]*$"
