#!/usr/bin/env python3

import sys
import pathlib

sys.path.append(str(pathlib.Path(__file__).parent.parent / 'project_management'))
from executor import Executor # type: ignore


if __name__ == "__main__":
    additional_arguments = [
        {
            'flag': '-b',
            'name': '--build',
            'help': 'Build the project.'
        },
        {
            'flag': '-t',
            'name': '--test',
            'help': 'Run tests.'
        },
    ]

    ex = Executor(additional_arguments, description='Execute feature tests')

    if ex.arguments.build:
        commands = (
            'gradle build'
        )
    elif ex.arguments.test:
        commands = (
            './run-tests.sh'
        )
    else:
        commands = None

    ex.run(commands)
