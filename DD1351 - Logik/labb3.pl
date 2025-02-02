% For SICStus, uncomment line below: (needed for member/2)
%:- use_module(library(lists)).
% Load model, initial state and formula from file.
verify(Input) :-
  see(Input),
  read(T),
  read(L),
  read(S),
  read(F),
  seen,
  check(T, L, S, [], F).

% check(T, L, S, U, F)
% T - The transitions in form of adjacency lists
% L - The labeling
% S - Current state
% U - Currently recorded states
% F - CTL Formula to check.
%
% Should evaluate to true iff the sequent below is valid.
%
% (T,L), S |- F
% U
% To execute: consult(’your_file.pl’). verify(’input.txt’).

% Literals
% Check if true
check(_, L, S, [], X) :-
  member([S, States], L),
  member(X, States).

% Neg
check(_, L, S, [], neg(X)) :-
  member([S, States], L),
  \+member(X , States).

% And - Both F and G have to be true and evaluated separately
check(T, L, S, [], and(F,G)) :-
  check(T, L, S, [], F),
  check(T, L, S, [], G).

% Or1 - Either F or G have to be true.
check(T, L, S, [], or(F, _)) :-
  check(T, L, S, [], F).

% Or2
check(T, L, S, [], or(_, G)) :-
  check(T, L, S, [], G).

% AX - Checks that the formula F is valid for all neighbours
check(T, L, S, _, ax(F)):-
  member([S, Neighbours], T),
  branching(T, L, [], F, Neighbours). % Checks all neighbours

% AG1 - Checks if path is finished
check(_, _, S, U, ag(_)):-
  member(S, U).

% AG2 - Formula F has to be valid in all accessible nodes
check(T, L, S, U, ag(F)):-
  \+member(S, U),
  check(T, L, S, [], F),
  member([S, Neighbours], T),
  branching(T, L, [S|U], ag(F), Neighbours). % Checks all neighbours

% AF1 - Checks that formula F is valid.
check(T, L, S, U, af(F)):-
  \+member(S, U),
  check(T, L, S, [], F).

% AF2 - For all paths formula F has to be valid at some point
check(T, L, S, U, af(F)):-
  \+member(S, U),
  member([S, Neighbours], T),
  branching(T, L, [S|U], af(F), Neighbours). % Checks all neighbours

% EX - Checks that forula F is valid in one neighbouring state
check(T, L, S, _, ex(F)) :-
  member([S, Neighbours], T), % Gets list of neighbours to current state
  member(NewState, Neighbours), % Selects one neighbouring state
  check(T, L, NewState, [], F).

% EG1 - Checks if path is finished
check(_, _, S, U, eg(_)):-
  member(S, U).

% EG2 - Checks that on one path formula F is always valid
check(T, L, S, U, eg(F)):-
  \+member(S, U),
  check(T, L, S, [], F),
  member([S, Neighbours], T), % Gets list of neighbours to current state
  member(NewState, Neighbours), % Selects one neighbouring state
  check(T, L, NewState, [S|U], eg(F)).

% EF1 - Checks that formula F is valid
check(T, L, S, U, ef(F)):-
  \+member(S, U),
  check(T, L, S, [], F).

% EF2 - Checks that on one path formula F is valid at some point
check(T, L, S, U, ef(F)):-
  \+member(S, U),
  member([S, Neighbours], T), % Gets list of neighbours to current state
  member(NewState, Neighbours), % Selects one neighbouring state
  check(T, L, NewState, [S|U], ef(F)).

% Makes a new check for each available neighbour based on given F and U
branching(T, L, U, F, [H|[]]):-
  check(T, L, H, U, F).

branching(T, L, U, F, [H|NTail]):-
  check(T, L, H, U, F), % Creates new branch
  branching(T, L, U, F, NTail).
