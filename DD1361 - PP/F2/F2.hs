module F2 where
import Data.List

-- Author: Pontus Curtsson pontuscu@kth.se

---------- TASK 1.1 --------------------------------------------------------------
-- A datatype MolSeq consiting of a sequence name, the sequence itself, and another datatype called SequenceType
-- regarding if it's DNA or Protein
data MolSeq = MolSeq {sequenceName :: String
                     ,sequence :: String
                     ,sequenceType :: SequenceType
                     } deriving (Show, Eq, Read)

-- A datatype that is either DNA or Protein
data SequenceType = DNA | Protein deriving (Show, Eq, Read)

---------- TASK 1.2 --------------------------------------------------------------
-- Takes a String for the name of the sequence, then the sequence in String and returns a MolSeq construct
string2seq :: String -> String -> MolSeq
string2seq sequenceName seqSequence
    |is_dna seqSequence = MolSeq sequenceName seqSequence DNA
    |otherwise = MolSeq sequenceName seqSequence Protein


is_dna :: String -> Bool
is_dna [] = True
is_dna (x:xs)
    |elem x "ACGT" = is_dna xs
    |otherwise = False

---------- TASK 1.3 --------------------------------------------------------------
-- Three "get" functions that return the name of the sequence, the sequence or its length
seqName :: MolSeq -> String
seqName (MolSeq name _ _) = name

seqSequence :: MolSeq -> String
seqSequence (MolSeq _ seqSequence _) = seqSequence

seqLength :: MolSeq -> Int
seqLength (MolSeq _ seqSequence _) = length seqSequence

---------- TASK 1.4 --------------------------------------------------------------
-- Calculates the distance between two DNA or Protein sequences
-- Checks for correct input
seqDistance :: MolSeq -> MolSeq -> Double
seqDistance firstMol secondMol
    |seqType firstMol /= seqType secondMol = error "Not same sequence type!"
    |seqType firstMol == DNA = jukes_cantor (normalizedHamming firstMol secondMol)
    |otherwise = poisson (normalizedHamming firstMol secondMol)

-- Calculates the distance between two DNA sequences using jukes-cantor based on given normalized Hamming distance
jukes_cantor :: Double -> Double
jukes_cantor alfa
    |alfa <= 0.74 = -3/4 * log(1 - alfa * 4/3)
    |otherwise = 3.3

-- Calculate the distance between two Protein sequences using poisson based on given normalized Hamming distance
poisson :: Double -> Double
poisson alfa
    |alfa <= 0.94 = -19/20 * log(1 - alfa * 20/19)
    |otherwise = 3.7

-- Returns the type of a MolSeq
seqType :: MolSeq -> SequenceType
seqType (MolSeq _ _ molType) = molType

-- Calculates the normalized hamming distance between two MolSeq's sequences
normalizedHamming :: MolSeq -> MolSeq -> Double
normalizedHamming firstMol secondMol =
    fromIntegral (hammingDistance (seqSequence firstMol) (seqSequence secondMol)) / fromIntegral (length (seqSequence firstMol))

-- calculates the hamming distance between two strings
hammingDistance :: String -> String -> Int
hammingDistance [] [] = 0
hammingDistance (x:xs) (y:ys)
    |x == y = 0 + hammingDistance xs ys
    |otherwise = 1 + hammingDistance xs ys


--- Given code to construct profile ---
nucleotides = "ACGT"
aminoacids = sort "ARNDCEQGHILKMFPSTWYVX"

makeProfileMatrix :: [MolSeq] -> [[(Char, Int)]]
makeProfileMatrix [] = error "Empty sequence list"
makeProfileMatrix sl = res
    where
        t = seqType(head sl)
        defaults =
            if (t == DNA) then
                zip nucleotides (replicate (length nucleotides) 0)
                -- Rad (i), creates a list [(A,0), (B,0)...] for all nucleotides by zipping together "ACGT" and a list of 0's
                -- the length of the String "ACGT"
            else
                zip aminoacids (replicate (length aminoacids) 0)
                -- Rad (ii), creates a list as above for all aminoacids.
        strs = map seqSequence sl
        -- Rad (iii), creates a list consisting of all given MolSeq's sequences

        tmpl = map (map (\x -> ((head x), (length x))) . group . sort)
                    (transpose strs)
        -- Rad (iv), transposes the strs list so that the head of the new list consists of all sequences first element, second list
        -- consists of the second element of the sequences etc. It then sorts and groups each list and creates a new list containing the
        -- pair in the lambda function, the occuring element x and the amount of those that occur. Because of the group function if a String
        -- was for example "aaabbccd" it would become ["aaa","bb","cc","d"] so for a this would be a pair like ("a", 3)

        -- fst takes out the first component of a pair, used to compare the pairs from the previous function.
        equalFst a b = (fst a) == (fst b)

        -- At this stage the tmpl list is complete in that it counts the occurences of each element correctly for each position.
        -- However it does not contain any pair of elements that did not occur. The below creates a list consisting of all pairs
        -- of nucelotides/aminoacids of the sort [("A",0), ("C",0)...] and adds the ones that don't exist via the equalFst condition
        -- and the unionBy.
        res = map sort (map (\l -> unionBy equalFst l defaults) tmpl)

---------- TASK 2.1 --------------------------------------------------------------
-- A profile that stores a matrix of sequences, the amount of sequences and the name of the profile
data Profile = Profile  {matrix :: [[(Char, Int)]]
                        ,amountOfSequences :: Int
                        ,nameOfProfile :: String
                        ,sequencesType :: SequenceType
                        } deriving (Show, Eq, Read)

---------- TASK 2.2 --------------------------------------------------------------
-- Creates a profile with a given name from a list of MolSeq's
molseqs2profile :: String -> [MolSeq] -> Profile
molseqs2profile str lst = Profile (makeProfileMatrix lst) (length lst) str (seqType (head lst))

---------- TASK 2.3 --------------------------------------------------------------
-- Returns the profiles given name
profileName :: Profile -> String
profileName (Profile _ _ name _) = name

-- Returns the profiles amount of sequences
profileAmountOfSequences :: Profile -> Int
profileAmountOfSequences (Profile _ amountOfSequences _ _) = amountOfSequences

-- Returns the profiles matrix
profileMatrix :: Profile -> [[(Char, Int)]]
profileMatrix (Profile matrix _ _ _) = matrix

-- Calculates a given chars relative frequency. Checks the given char in given position and dividies it by amount of sequences
-- to get the relative frequency.
profileFrequency :: Profile -> Int -> Char -> Double
profileFrequency pr position char = relativeFreq
    where
        amountOfSequences = profileAmountOfSequences pr
        -- Pick out the correct list based on given positon and find freq of given char
        freq = findCharFreq ((profileMatrix pr)!!position) char
        relativeFreq = fromIntegral freq / fromIntegral amountOfSequences

-- Checks for given Char in a list containing pairs. If the fst of a pair matches given char, it returns the snd of that pair
findCharFreq :: [(Char, Int)] -> Char -> Int
findCharFreq (x:xs) charToFind
    |fst x == charToFind = snd x
    |otherwise = findCharFreq xs charToFind

---------- TASK 2.4 --------------------------------------------------------------
-- Calculates the distance of two given profiles by first transforming each into matrices with the relative frequency values.
-- Then goes through both matrices and calculates the sum specified by the formula
profileDistance :: Profile -> Profile -> Double
profileDistance profileOne profileTwo = distanceMatrix
    where
        -- Transforms both matrices
        tMatrixOne = transformToRelativeMatrix (profileMatrix profileOne) (profileAmountOfSequences profileOne)
        tMatrixTwo = transformToRelativeMatrix (profileMatrix profileTwo) (profileAmountOfSequences profileTwo)
        -- Calculates the sum
        distanceMatrix = calculateDistance tMatrixOne tMatrixTwo

-- Sums up the sum over each list as specified by the formula, using sumList to iterate over all the pairs
calculateDistance :: [[(Char, Double)]] -> [[(Char, Double)]] -> Double
calculateDistance [] [] = 0.0
calculateDistance mOne mTwo = (sumList (head mOne) (head mTwo)) + calculateDistance (tail mOne) (tail mTwo)

-- Calculates the sum of each individual list as specified by the given formula, abs(m(i,j) - m'(i,j))
sumList :: [(Char, Double)] -> [(Char, Double)] -> Double
sumList [] [] = 0.0
sumList fstLst sndLst = abs((snd (head fstLst)) - (snd (head sndLst))) + sumList (tail fstLst) (tail sndLst)

-- For each pair in every list it calculates the relative frequency by dividing its frequency by the amount of sequences
-- Then returns a new matrix with relative frequency
transformToRelativeMatrix :: [[(Char, Int)]] -> Int -> [[(Char, Double)]]
transformToRelativeMatrix lst amountOfSequences =
    -- for each pair replace its frequnecy by its relative frequency
    map (map (\(x,y) -> (x, (fromIntegral y / fromIntegral amountOfSequences)))) lst

---------- TASK 3.1 & 3.2 --------------------------------------------------------------
-- Contains methods to calculate distance between object a's.
class Evol a where
    name :: a -> String
    distance :: a -> a -> Double
    -- Calculates the distance between a list of object a's and returns them in the format [(String (Name),String (Name), Distance),...]
    distanceMatrix :: [a] -> [(String, String, Double)]
    distanceMatrix [] = []
    distanceMatrix lst = calculatedLst (head lst) lst ++ distanceMatrix (tail lst)
    -- Helper that calculates the distance of one given objec a against all other in the list
    calculatedLst :: a -> [a] -> [(String, String, Double)]
    calculatedLst _ [] = []
    calculatedLst first lst = ((name first), (name (head lst)), distance first (head lst)) : calculatedLst first (tail lst)

instance Evol MolSeq where
    name = seqName
    distance = seqDistance

instance Evol Profile where
    name = profileName
    distance = profileDistance
