
% Author: Pontus Curtsson pontuscu@kth.se

% --------------------- TASK 1 --------------------
% N signifies which fibonacci number, and F is the value of the given number.
% Since this solutions complexity isnt n^2, can easily calculate large numbers.
fib(0, 0).
fib(F, N) :-
    F > 0,
    fib(F, N, _).

% below calculates fib in a tail-recursive manner
fib(1,1,0).
fib(F, N1, N2) :-
    F > 1,
    F1 is F - 1,
    fib(F1, N2, N3),
    N1 is N2 + N3.

% --------------------- TASK 2 --------------------
% Changes given list of ascii values to rovarsprak, or vice versa.
% First predicate checks if head of string is a vowel, and then continues on with the tail.
rovarsprak([X|XS],[X|XY]):-
    memberchk(X,[97, 101, 105, 111, 117, 121]),
    !,
    rovarsprak(XS,XY).

% Second predicate checks that it fulfills the
% pattern of "letter-"o"-letter" for the rovarsprak string.
rovarsprak([X|XS],[X,111,X|XY]):-
    rovarsprak(XS,XY).

% Ending statement when both strings are empty.
rovarsprak([],[]).

% --------------------- TASK 3 --------------------
% calculates average length of all words in given string.
medellangd(Text, AvgLen):-
    count_words_letters(Text, L, W),
    !,
    AvgLen is (L/W).

% end statement if list ends with a letter
count_words_letters([X|[]], 1, 1):-
    char_type(X, alpha).

% Predicate if a word ends, and if so counts up 1 for letter and word
count_words_letters([X,Y|XS], L, W):-
    char_type(X, alpha),
    \+char_type(Y, alpha),
    count_words_letters(XS, L1, W1),
    L is L1+1,
    W is W1+1.

% Predicate to count letters in word.
count_words_letters([X|XS], L, W):-
    char_type(X, alpha),
    count_words_letters(XS, L1, W1),
    L is L1+1,
    W is W1.

% Predicate that handles if a string contains multiple non-alphabet
% characters in beginning or succession
count_words_letters([X|XS], L, W):-
    \+char_type(X, alpha),
    count_words_letters(XS, L1, W1),
    L is L1,
    W is W1.

% When string is empty, return base values.
count_words_letters([], 0, 0).

% --------------------- TASK 4 --------------------
% Shuffles a list based on given algorithm
skyffla([], []):-!.

skyffla(X, Skyfflad):-
    select_every_other(X, Y),
    remove_head(X, XS),
    select_every_other(XS, Y2),
    skyffla(Y2, Y3),
    !,
    append(Y, Y3, Skyfflad).

% removes the head of a list
remove_head([], []).
remove_head([_|XS], XS).

% Selects every other element starting from head, e.g. [1,2,3,4,5] -> [1,3,5]
select_every_other([X,_|XS],Y):-
    select_every_other(XS, Y1),
    !,
    append([X], Y1, Y).

select_every_other([X|[]], [X]).

select_every_other([], []).
