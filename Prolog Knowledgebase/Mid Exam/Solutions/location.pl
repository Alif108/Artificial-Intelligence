%%% facts %%%
is_in(me, lab4).
is_in(lab4, cse).
is_in(cse, uap).

%%% rules %%%
located_at(X, Y):-
	is_in(X, Y).

located_at(X, Y):-
	is_in(X, Z),
	located_at(Z, Y).

%%% queries %%%
% ?- located_at(me, X).
% X = lab4 ;
% X = cse ;
% X = uap ;
% false.

% ?- located_at(X, uap).
% X = cse ;
% X = me ;
% X = lab4 ;
% false.