
% Reads input and validates
verify(InputFileName) :-
  see(InputFileName),
  read(Prems),
  read(Goal),
  read(Proof),
  seen,
  valid_proof(Prems, Goal, Proof, []).

% --------- Validates proof
valid_proof(_, Goal, [], [[_, Goal, _]|_]). % End predicate for when proof is empty

% Assumption rule - also opens a box since that is required upon an assumption
valid_proof(Prems, Goal, [[[Row, Term, assumption]|BoxTail]|ProofT], ValidatedProof):-
  valid_box(Prems, Goal, BoxTail, [[Row, Term, assumption]|ValidatedProof]),
  valid_proof(Prems, Goal, ProofT, [[[Row, Term, assumption]|BoxTail]|ValidatedProof]).

% Checks if there is a valid rule for the next row in the proof
valid_proof(Prems, Goal, [ProofH|ProofT], ValidatedProof):-
  valid_rule(Prems, Goal, ProofH, ValidatedProof),
  valid_proof(Prems, Goal, ProofT, [ProofH|ValidatedProof]).

% --------- Box management
valid_box(Prems, Goal, [ProofH|[]], ValidatedProof):- % End predicate for when box is empty
  valid_rule(Prems, Goal, ProofH, ValidatedProof).

valid_box(_, _, [], _). % If no boxtail after assumption

% Box in a box in a box
valid_box(Prems, Goal, [[[Row, Term, assumption]|BoxTail]|ProofT], ValidatedProof):-
  valid_box(Prems, Goal, BoxTail, [[Row, Term, assumption]|ValidatedProof]),
  valid_box(Prems, Goal, ProofT, [[[Row, Term, assumption]|BoxTail]|ValidatedProof]).

% Checks if there is a valid rule for the next row in the proof
valid_box(Prems, Goal, [ProofH|ProofT], ValidatedProof):-
  valid_rule(Prems, Goal, ProofH, ValidatedProof),
  valid_box(Prems, Goal, ProofT, [ProofH|ValidatedProof]).

% Searches a list for another list where the first element matches Assumption
find_box(Assumption, [ValidH|_], ValidH):-
  check_box(Assumption, ValidH).

find_box(Assumption, [_|ValidT], Box):-
  find_box(Assumption, ValidT, Box).

% Checks if first element of list contains Assumption
check_box([Row, Term, Assumption], [[Row, Term, Assumption]|_]).

% --------- Rules
% Premise
valid_rule(Prems, _, [_, Term, premise], _):-
  %write('premise'),
  memberchk(Term, Prems).

% Copy
valid_rule(_, _, [_, Term, copy(RowToCopy)], ValidatedProof):-
  %write('copy'),
  memberchk([RowToCopy, Term, _], ValidatedProof).

% Andint
valid_rule(_, _, [_, and(FirstTerm, SecTerm), andint(FirstRow, SecRow)], ValidatedProof):-
  %write('andint'),
  memberchk([FirstRow, FirstTerm, _], ValidatedProof),
  memberchk([SecRow, SecTerm, _], ValidatedProof).

% Andel1
valid_rule(_, _, [_, Term, andel1(Row)], ValidatedProof):-
  %write('andel1'),
  memberchk([Row, and(Term, _), _], ValidatedProof).

% Andel2
valid_rule(_, _, [_, Term, andel2(Row)], ValidatedProof):-
  %write('andel2'),
  memberchk([Row, and(_, Term), _], ValidatedProof).

% Orint1
valid_rule(_, _, [_, or(Term, _), orint1(Row)], ValidatedProof):-
  %write('orint1'),
  memberchk([Row, Term, _], ValidatedProof).

% Orint2
valid_rule(_, _, [_, or(_, Term), orint2(Row)], ValidatedProof):-
  %write('orint2'),
  memberchk([Row, Term, _], ValidatedProof).

% Orel
valid_rule(_, _, [_, _, orel(OrRow, FirstFirst, FirstSec, SecFirst, SecSec)], ValidatedProof):-
  %write('orel'),
  memberchk([OrRow, or(FirstTerm, SecTerm), _], ValidatedProof),
  find_box([FirstFirst, FirstTerm, assumption], ValidatedProof, BoxOne),
  last(BoxOne, [FirstSec, Term, _]),
  find_box([SecFirst, SecTerm, assumption], ValidatedProof, BoxTwo),
  last(BoxTwo, [SecSec, Term, _]).

% Impint
valid_rule(_, _, [_, imp(FirstTerm, SecTerm), impint(FirstRow, SecRow)], ValidatedProof):-
  %write('impint'),
  find_box([FirstRow, FirstTerm, assumption], ValidatedProof, Box),
  last(Box, [SecRow, SecTerm, _]).

% Impel
valid_rule(_, _, [_, Term, impel(RowElement, RowImplication)], ValidatedProof):-
  %write('impel'),
  memberchk([RowElement, ElimTerm, _], ValidatedProof),
  memberchk([RowImplication, imp(ElimTerm, Term), _], ValidatedProof).

% Negint
valid_rule(_, _, [_, neg(Term), negint(FirstRow, SecRow)], ValidatedProof):-
  %write('negint'),
  find_box([FirstRow, Term, assumption], ValidatedProof, Box),
  last(Box, [SecRow, cont, _]).

% Negel
valid_rule(_, _, [_, cont, negel(FirstRow, SecRow)], ValidatedProof):-
  %write('negel'),
  memberchk([FirstRow, Term, _], ValidatedProof),
  memberchk([SecRow, neg(Term), _], ValidatedProof).

% Contel
valid_rule(_, _, [_, _, contel(Row)], ValidatedProof):-
  %write('contel'),
  memberchk([Row, cont, _], ValidatedProof).

% Negnegint
valid_rule(_, _, [_, neg(neg(Term)), negnegint(Row)], ValidatedProof):-
  %write('negnegint'),
  memberchk([Row, Term, _], ValidatedProof).

% Negnegel
valid_rule(_, _, [_, Term, negnegel(Row)], ValidatedProof):-
  %write('negnegel'),
  memberchk([Row, neg(neg(Term)), _], ValidatedProof).

% Mt
valid_rule(_, _, [_, neg(Term), mt(FirstRow, SecRow)], ValidatedProof):-
  %write('mt'),
  memberchk([FirstRow, imp(Term, SecTerm), _], ValidatedProof),
  memberchk([SecRow, neg(SecTerm), _], ValidatedProof).

% Pbc
valid_rule(_, _, [_, Term, pbc(FirstRow, SecRow)], ValidatedProof):-
  %write('pbc'),
  find_box([FirstRow, neg(Term), assumption], ValidatedProof, Box),
  last(Box, [SecRow, cont, _]).

% LEM
valid_rule(_, _, [_, or(Term, neg(Term)), lem], _).
