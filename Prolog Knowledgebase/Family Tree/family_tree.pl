%%%%%%% ------ facts ------ %%%%%%%
male(mohon).
male(sayeed).
male(ali).
male(kashem).
male(oli).
male(haque).
male(jakir).
male(sumso).
male(motaleb).
male(kadir).
male(rayhan).
male(amir).
male(fahim).
male(rabbi).

female(jesmin).
female(shabmeher).
female(jamila).
female(saleha).
female(munni).
female(firoza).
female(rubi).
female(fatema).
female(nahar).
female(ara).
female(eva).
female(mim).
female(lamia).
female(jakia).
female(sumi).
female(srity).


% A is parent of B
parent(mohon,sayeed).
parent(mohon,ali).
parent(sayeed,kashem).
parent(sayeed,oli).
parent(sayeed,haque).
parent(ali,jakir).
parent(ali,fatema).
parent(ali,sumso).
parent(kashem,motaleb).
parent(kashem,kadir).
parent(haque,rayhan).
parent(haque,mim).
parent(jakir,jakia).
parent(sumso,amir).
parent(sumso,fahim).
parent(sumso,rabbi).


%%%%%%% ------ rules ------ %%%%%%%
/*
X and Y are siblings if
X and Y have the same parent and
X and Y are not the same person
*/
siblings(X, Y):-
    parent(Z, X),
    parent(Z, Y),
    X\=Y.

/*
X is an Uncle of Y if
Z is a parent of Y and
Z and X are siblings and
X is a male
*/
uncle(X, Y):-
    parent(Z, Y),
    siblings(X, Z),
    male(X).


/*
X is a Grandparent of Y if 
X is parent of Z and Z is parent of Y
*/
grandparent(X, Y):-
    parent(X, Z),
    parent(Z, Y).

/*
X is Great Grandparent of Y if
X is parent of Z and Z is grandparent of Y
*/
greatgrandparent(X, Y):-
    parent(X, Z),
    grandparent(Z, Y).

/*
X and Y are first cousins if
they have the same grandparent and
they are not siblings and
they are not the same person
*/
first_cousin(X, Y):-
    grandparent(Z, X),
    grandparent(Z, Y),
    not(siblings(X, Y)),
    X\=Y.

/*
X and Y are second cousins if
they have the same great grandparent and
they are not siblings and
they are not first cousins
they are not the same person
*/
second_cousin(X, Y):-
    greatgrandparent(Z, X),
    greatgrandparent(Z, Y),
    not(siblings(X, Y)),
    not(first_cousin(X, Y)),
    X\=Y.

/*
X and Y are first cousins once removed if
    Z is the parent of Y and 
    X and Z are first cousins
    or vice versa
*/
first_cousin_once_removed(X, Y):-
    (
        parent(Z, Y),
        first_cousin(X, Z)
    );
    (
        parent(Z, X),
        first_cousin(Y, Z)
    ).

/*
X and Y are first cousins twice removed if
    Z is the grandparent of Y and 
    X and Z are first cousins
    or vice versa
*/
first_cousin_twice_removed(X, Y):-
    (
        grandparent(Z, Y),
        first_cousin(X, Z)
    );
    (
        grandparent(Z, X),
        first_cousin(Y, Z)
    ).