%%% facts %%%
eats(bug, grass).
eats(frog, bug).
eats(snake, frog).

%%% rules %%%
digests(Predator, Prey):-
    eats(Predator, Prey).

digests(Predator, Prey):-
    eats(Predator, X),
    digests(X, Prey).

%%% queries %%%
% ?- digests(snake, X).
% X = frog ;
% X = bug ;
% X = grass ;
% false.

% ?- digests(frog, X).
% X = bug ;
% X = grass ;
% false.

% ?- digests(X, grass).
% X = bug ;
% X = frog ;
% X = snake ;
% false.