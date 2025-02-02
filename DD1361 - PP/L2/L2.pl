
% Author: Pontus Curtsson pontuscu@kth.se

% Checks if spider
spider(X):-
    person(X),
    is_spider(X).

% Evaluates if given X is a spider.
is_spider(X):-
    findall(Z, friends(X, Z), SpiderFriends),
    findall(Y, person(Y), PList),
    % Covers the case that everyone has to know either spider or conspirator
    all_connected(PList, [X|SpiderFriends]),
    % Generates all permutations of conspirators
    generate_conspirators(SpiderFriends, Conspirators),
    no_friends_in_list(Conspirators),
    subtract(PList, [X|Conspirators], OutsidePeople),
    % Covers the case that everyone that isnt conspirator or spider hsa to know a conspirator
    all_connected(OutsidePeople, Conspirators),
    !.

% Generates all possible conspirators
generate_conspirators(X, Y):-
    generate_conspirators(X, Y, []).

generate_conspirators([], Y, Y).
generate_conspirators([X|XS], Y, C):-
    check_valid_conspirators([X|XS], C),
    remove_friends(X, XS, X2),
    remove_friends(X, C, C2),
    generate_conspirators(X2, Y, [X|C2]).

generate_conspirators([_|XS], Y, C):-
    check_valid_conspirators(XS, C),
    generate_conspirators(XS, Y, C).

% Checks that all people outside of X and Y know someone in X
check_valid_conspirators(X, Y):-
    append(X, Y, XY),
    findall(Z, person(Z), Persons),
    subtract(Persons, XY, OutsidePeople),
    all_connected(OutsidePeople, XY).

% Makes sure everone in list X knows someone in list Y
all_connected([], _).
all_connected([X|XS], Y):-
    !,
    connected(X, Y),
    all_connected(XS, Y).

% Checks that given X is friends with someone in list Y.
connected([], _):- !, false.
connected(X, [Y|_]):-
    friends(X, Y),
    !.

connected(X, [_|YS]):-
    connected(X, YS).

% removes all friends of X from given list.
remove_friends(X, Y, R):-
    remove_friends(X, Y, R, []).

remove_friends(X, [Y|YS], R, NF):-
    friends(X, Y),
    !,
    remove_friends(X, YS, R, NF).

remove_friends(X, [Y|YS], R, NF):-
    !,
    remove_friends(X, YS, R, [Y|NF]).
remove_friends(_, [], R, R):-!.

% Checks that no person in X has any friends in X.
no_friends_in_list([]).
no_friends_in_list([X|XS]):-
    no_friends_in_list(X, XS),
    no_friends_in_list(XS).

no_friends_in_list(X, [Y|YS]):-
    \+friends(X, Y),
    !,
    no_friends_in_list(X, YS).

no_friends_in_list(_, []):- !.

% Checks if X or Y know each other.
friends(X, Y):-
    knows(X, Y);
    knows(Y, X).
