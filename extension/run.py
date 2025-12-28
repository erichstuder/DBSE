#!/usr/bin/env python3

import sys
import pathlib

sys.path.append(str(pathlib.Path(__file__).parent.parent / 'project_management'))
from executor import Executor # type: ignore


if __name__ == "__main__":
    additional_arguments = [
        {
            'flag': '-t',
            'name': '--test',
            'help': 'Run tests.'
        },
    ]

    ex = Executor(additional_arguments, description='Execute feature tests')

    if ex.arguments.test:
        commands = 'cd parser && ./run-tests.sh'
    else:
        commands = None

    ex.run(commands)
