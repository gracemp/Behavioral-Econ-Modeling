# Behavioral-Econ-Modeling
Result of my Fall 2015 work with Professor Joel Myerson of the Washington University Psychology Department.
The src file holds the Java version, and the program.r file contains the same concept translated into R.

====================================================================================================================================
A program intended to approximate  collected data from behavioral economics experiments. 
Inputted data is from an experiment asking participants for the amount of money they would accept today in exchange for money given in the future ( time delay) and at only a certain probaility (odds-against)
This program takes in the delayed probabalilistic amount, the number of subjects,the conditions for delay ( 1 month delay, 6 month delay, etc) and odds against ( 30% chance of receiving, 50% chance, etc) and each subjects responses.

Using a supporting object of a (x, y, z) point I 'plotted' each point for each subject, and made 'boxes' out of each set of 4 adjacent data points. This program outputs the percent of the maximum possible area for each subject from the sum of the volumes of all the boxes. This is essentially finding the Riemann integral for each subject.

