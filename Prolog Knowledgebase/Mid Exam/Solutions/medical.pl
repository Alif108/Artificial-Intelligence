%%% Facts %%%
symptom(dwight, fever).
symptom(dwight, cough).
symptom(dwight, shortness_of_breath).
symptom(dwight, fatigue).
symptom(dwight, headache).
symptom(dwight, abdominal_pain).
symptom(dwight, constipation).

symptom(jim, fever).
symptom(jim, headache).
symptom(jim, abdominal_pain).
symptom(jim, constipation).

symptom(pam, fever).
symptom(pam, headache).
symptom(pam, muscle_pain).
symptom(pam, joint_pain).

symptom(ryan, fever).
symptom(ryan, headache).
symptom(ryan, muscle_pain).
symptom(ryan, joint_pain).

symptom(michael, abdominal_pain).
symptom(michael, loose_motion).
symptom(michael, fever).
symptom(michael, nausea).

%%% Rules %%%
diagnose(Patient, covid) :-
    symptom(Patient, fever),
    symptom(Patient, cough),
    symptom(Patient, shortness_of_breath),
    symptom(Patient, fatigue).

diagnose(Patient, typhoid) :-
    symptom(Patient, fever),
    symptom(Patient, headache),
    symptom(Patient, abdominal_pain),
    symptom(Patient, constipation).

diagnose(Patient, dengue) :-
    symptom(Patient, fever),
    symptom(Patient, headache),
    symptom(Patient, muscle_pain),
    symptom(Patient, joint_pain).

diagnose(Patient, diarrhea) :-
    symptom(Patient, abdominal_pain),
    symptom(Patient, loose_motion),
    symptom(Patient, fever),
    symptom(Patient, nausea).

%%% Queries %%%
% ?- diagnose(dwight, X).
% X = covid ;
% X = typhoid ;
% false.

% ?- diagnose(X, dengue).
% X = pam ;
% X = ryan ;
% false.

% ?- diagnose(michael, diarrhea).
% true.

% ?- diagnose(michael, covid).
% false. 