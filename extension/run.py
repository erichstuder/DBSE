#!/usr/bin/env python3

import sys
import pathlib

sys.path.append(str(pathlib.Path(__file__).parent.parent / 'project_management'))
from executor import Executor # type: ignore


if __name__ == "__main__":
    additional_arguments = [
        {
            'flag': '-i',
            'name': '--init',
            'help': 'Initialize the project. Must be run before working on the project.'
        },
        {
            'flag': '-t',
            'name': '--test',
            'help': 'Run tests.'
        },
    ]

    ex = Executor(additional_arguments, description='Execute feature tests')

    if ex.arguments.init:
        commands = (
            'Xvfb :99 & export DISPLAY=:99 && '
            './parser/build.sh && '
            'npm install --prefix ./sysmlv2-renderer && '
            'cd sysmlv2-renderer && npx vscode-test --download-only'
        )
    elif ex.arguments.test:
        commands = (
            'Xvfb :99 & export DISPLAY=:99 && '
            'echo "" && echo "*** Running extension tests..." && '
            'npm test --prefix ./sysmlv2-renderer && '
            'echo "" && echo "*** Running parser tests..." && '
            './parser/run-tests.sh'
        )
    else:
        commands = None

    ex.run(commands)
