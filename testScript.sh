#!/usr/bin/env bash

###########
# TESTS inputs into the java program
# -- Current Implementation --
#  runs all the input files against the java application
#  we tested the output from this script to the outputs from the .bto.txt files manually to confirm
#
#  Currently working without failure:
#       buy
#       login
#       logout
#
#  Plan to compare automatically to the output files in the future
###########


# for all files with extension .inp.txt
for i in tickets/src/tests/textfiles/*/*.inp.txt; do
    # split the input files by line and put into array 'lines'
    IFS=$'\n' read -d '' -r -a lines < $i

    # formatting for test output
    echo "-- -- -- -- -- "
    echo $i
    echo '========'

    # for each line, add to the scannerInput string which is what is giving input to the java file
    for input in "${lines[@]}"
    do
        scannerInput+="echo $input;"
        scannerInput+="sleep 1;"
    done

    # runs the java file with the input files inputs
    eval $scannerInput | java tickets/src/app/App.java
    lines=()
    scannerInput=''
done
