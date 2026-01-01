#!/usr/bin/env python3

from project_management.dispatcher import run_dispatcher

if __name__ == "__main__":
    scripts = {
        'extension': 'extension/run.py',
    }

    run_dispatcher(scripts)
