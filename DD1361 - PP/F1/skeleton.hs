module F1 where
import Data.Char(isAlpha)
-- Author: Pontus Curtsson pontuscu@kth.se

---------- TASK 1 --------------------------------------------------------------
-- Calculates fib number for given parameter.  Not a very fast implementation as it calculates the same fib numbers multiple times.
fib :: Integer -> Integer
fib 0 = 0
fib 1 = 1
fib n = fib(n-1) + fib(n - 2)

---------- TASK 2 --------------------------------------------------------------
-- Transforms a given String to rovarsprak
rovarsprak :: String -> String
rovarsprak [] = []
rovarsprak s = rovarsprak_replace(take 1 s) ++ rovarsprak(tail s)

-- Helper for rovarsprak. Takes a one character String and checks if its a consonant. If true it applies the rovarsprak formula.
-- Returns the transformed or non-transformed String
rovarsprak_replace :: String -> String
rovarsprak_replace s =
    if is_consonant(s)
        then s ++ "o" ++ s
        else s

-- Transforms a given String from rovarsprak
-- Checks first char, if it is a consonant it uses the first char together with removing the first 3 characters in the list to make a new one.
karpsravor :: String -> String
karpsravor [] = []
karpsravor s
      |is_consonant(take 1 s) = (take 1 s) ++ karpsravor(drop 3 s)
      |otherwise = (take 1 s) ++ karpsravor(tail s)

-- Checks if given one-character String is a consonant.
is_consonant :: String -> Bool
is_consonant x = elem x ["b","c","d","f","g","h","j","k","l","m","n","p","q","r","s","t","v","w","x","z"]

---------- TASK 3 --------------------------------------------------------------
-- Function that returns the average length of all words containted in given String
-- find_next_word makes sure it starts on a word
medellangd :: String -> Double
medellangd s = medellangd_helper (find_next_word s) 0 0

-- Helperfunction. Counts each letter in the given string and keeps track of that. If it finds that the next element in the String isnt a letter it counts it as a word.
-- Then it finds the next word and keeps going. Calculates the letters/word as a Double once the String is empty.
medellangd_helper :: String -> Integer -> Integer -> Double
medellangd_helper str numberOfWords numberOfLetters
    |null str = fromIntegral(numberOfLetters) / fromIntegral(numberOfWords)
    |isAlpha(head str) = medellangd_helper (tail str) (numberOfWords + (check_if_add_wordcount (tail str))) (numberOfLetters + 1)
    |otherwise = medellangd_helper (find_next_word str) (numberOfWords + 1) numberOfLetters

-- Finds the next word in a given String.
find_next_word :: String -> String
find_next_word str
    |null str = []
    |isAlpha(head str) = str
    |otherwise = find_next_word(tail str)

-- A function that checks if there are no more words in the list. If so it returns 1 which is added to the numberOfWords parameter.
-- Made it due to an issue that arose with my design when a String ended with a character that isn't part of the alphabet such as ",!2".
check_if_add_wordcount :: String -> Integer
check_if_add_wordcount str
    |null str = 1               -- It makes sure to add a wordcount if the String is ending, as the medellangd_helper function will not reach that statement if the String is empty.
    |otherwise = 0

---------- TASK 4 --------------------------------------------------------------
-- Shuffles a given list. Selects every other element and puts it in the front, then performs same operation on the remaining elements.
skyffla :: [a] -> [a]
skyffla [] = []
skyffla lst = select_every_other_element(lst) ++ skyffla(select_every_other_element(tail lst))

-- Function that selects every other element starting at the head of the given list
select_every_other_element :: [a] -> [a]
select_every_other_element [] = []
select_every_other_element lst = (take 1 lst) ++ select_every_other_element(drop 2 lst)
