from typing import Dict, Set


class WrongMoveError(Exception):
    pass


class Automaton:

    def __init__(self, config):
        with open(config) as f:
            parameters = f.readlines()
            self.S0 = parameters[1].strip()
            self.current_state = self.S0
            self.final_states = parameters[2].split()
            self.f = {}
            for move in parameters[3:]:
                move = move.split()
                if not move[0] in self.f:
                    self.f[move[0]] = {}
                if not move[2] in self.f:
                    self.f[move[2]] = {}
                self.f[move[0]][move[1]] = move[2]

    def move(self, word):
        for x in word:
            if x in self.f[self.current_state]:
                self.current_state = self.f[self.current_state][x]
            else:
                raise WrongMoveError
        return self.current_state


steps = []
used_states = []


def find_solution(start: str, finals: Set[str], f: Dict[str, Dict[str, str]]):
    if not finals:
        return
    used_states.append(start)
    if start in finals:
        return ''
    for move, next_state in f[start].items():
        step_made = False
        if next_state in finals:
            steps.append(move)
            return ''
        if next_state not in used_states:
            steps.append(move)
            step_made = True
            success = find_solution(next_state, finals, f)
        if success is not None and success != "end":
            return 'end'
        elif success == 'end':
            return success
        elif step_made:
            del steps[-1]


if __name__ == '__main__':
    automaton = Automaton(config="/home/oleksandr/lab_2_config.txt")

    move_1 = ""
    new_start = automaton.move(move_1)

    move_2 = "x"
    new_finals = []
    for state in automaton.f:
        automaton.current_state = state
        try:
            if automaton.move(word=move_2) in automaton.final_states:
                new_finals.append(state)
        except WrongMoveError:
            pass

    find_solution(start=new_start, finals=new_finals, f=automaton.f)
    solution = "".join([move_1, ''.join(steps), move_2])

    try:
        automaton.current_state = automaton.S0
        if automaton.move(solution) in automaton.final_states:
            print(move_1, ''.join(steps), move_2)
        else:
            print('No solutions')
    except WrongMoveError:
        print("No solutions")
