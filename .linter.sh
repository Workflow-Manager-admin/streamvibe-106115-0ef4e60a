#!/bin/bash
cd /home/kavia/workspace/code-generation/streamvibe-106115-0ef4e60a/streamvibe
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

