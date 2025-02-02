% Fråga 1
% Inga bindningar

% Motivering: Det finns ingen substitution för att termerna ska bli syntaktiskt identiska. Således kan termerna inte unifieras.

% Fråga 2
fakta(a, b).

% Om man frågar fakta(a, X) kommer den kunna bevisa att fakta(a, b) då X unifieras med b stämmer.
% Däremot om man ställer frågan fakta(c, X) kommer X inte kunna unifieras och då blir det false, via failure.
% Samma gäller om man skulle ta fakta(b, X) då det inte finns något som kan unifieras där heller.

% Fråga 3
findlast([H|[]], R, H, R). % Stop condition
findlast([H|T], R, E, L):-
  append(L, [H], L2),
  findlast(T, R, E, L2).
findlast([H|[]], [], H). % Special case for list containing only 1 element
findlast([H|T], R, E) :- findlast(T, R, E, [H]).

% Kontrollflödet:
% Mål: findlast([1,2,3], R,E).
% matchar inte tredje regelnhalt.
% matchar fjärde regeln
% skapar en instans av fjärde regeln: findlast([H|T], R, E) :- findlast(T, R, E, [H]).
% Delmål: T=[2,3] H=1
% matchar inte första regeln
% matchar andra regeln
% skapar en instans av andra regeln: findlast([H|T], R, E, L):- append(L, [H], L2), findlast(T, R, E, L2).
% Delmål: T=[3] L2=[1,2]
% matchar första regeln: findlast([H|[]], R, H, R).
% Delmål: R=[1,2] H=3

% Fråga 4
% Två olika metoder. Båda returnerar en tom lista, vilken föredras?

% Akumulator
partstring([], F, F). % Stop condition
partstring([H|T], F, F2) :- % Stores head
  append(F2, [H], F3),
  partstring(T, F, F3).
partstring([_|T], F, F2) :- partstring(T, F, F2). % Discards head
partstring(X, F) :- partstring(X, F, []).

partstring2([_|[]], []).
partstring2([H|[]], [H]). % Stores head
partstring2([_|T], F) :- partstring2(T, F). % Discards head
partstring2([H|T], [H|F]) :- partstring2(T, F).

% Fråga 5

permute([], []).
permute([H|T], F) :-
  permute(T, F1),
  append(F2, F3, F1),
  append(F2, [H|F3], F).

% Kontrollflödet:
% Funktionen utnyttjar två anrop till append. Det första ger variationer på listan och den andra sätter ihop listan med tidigare element.

% Mål: permute([1,2], X)
% Skapar instans av 2a regeln. (1)
% Unifierar H = 1, T = [2].
% Skapar instans av 2a regeln. (2)
% Unifierar H = 2, T = [].
% Skapar instans av 1a regeln. Går tillbaks till (2)
% Gör append(X, Y, []), uniferar X & Y = [].
% Gör append(X, [2|Y], F), uniferar F = [2].
% Går tillbaks till (1).
% Gör append(X, Y, [2]), unifierar X = [], Y = [2].
% Gör append([], [1|Y], F), uniferar F = [1,2].
% Uppnår delmål: X = [1,2].
% Går tillbaks till (1).
% Gör append(X,Y,[2]), unifierar X = [2], Y = [].
% Gör append(X, [1|Y], F), uniferar F = [2,1].
% Uppnår delmål: X = [2,1].
% Går tillbaks till (1), kan ej unifiera append(X, Y, [2]) igen.
% Går tillbaks till (2), kan ej unifiera append(X, Y, []) igen.
% Fail, kan ej uppnå fler delmål

% Fråga 6
%  Fakta för nodsystem
node(a, [b, c, f]).
node(b,[a, f]).
node(c, [a, d, e, f]).
node(d, [c, e, f]).
node(e, [c,d, f]).
node(f, [a,b,c,d,e]).

findnode(X, X, Z, Z). % Stop condition
findnode(X, Y, Z, Z1):-
  node(X, L),
  nextneighbour(Z1, L, N),
  append(Z1, [N], Z2),
  findnode(N, Y, Z, Z2). % Steps to next neighbour
findnode(X, Y, Z):- findnode(X, Y, Z, [X]). % X startnode, Y node to find, Z path to Y

% X is visited nodes, Y is current neighbours, Z next neighbour
% nextneighbour(X, Y, Z)
nextneighbour(X, [H|_], H):- \+ memberchk(H, X). % Finds working neighbour
nextneighbour(X, [_|T], Z):- nextneighbour(X, T, Z). % Didnt find neighbour, check next element
