# VocabularyFox

VocabularyFox is an open-source vocabulary quiz program, written in Java.

VocabularyFox
* Supports (but is not limited to) English and French vocabulary quizzes
* Lets you add your own vocabulary quizzes easily
* Includes a sample vocabulary quiz
* Lets you choose the language of the user interface (currently English and German, _if you like to contribute to adding another language, feel free to contact me at_ "lo (dot) wiest (at) web (dot) de").

I developed VocabularyFox on Windows 8.1 (64-bit), but it should be easy to port it to other platforms.

The VocabularyFox source code is available under the MIT license.

Enjoy -- Lorenz

## Table of Contents

* [Quick Start](#quick-start)
* [Visual Tour](#visual-tour)
* [How to Add Your Own Quizzes](#how-to-add-your-own-quizzes)

## Quick Start

I have prepared for you a ready-to-run VocabularyFox file for the Windows (64-bit) platform. To run it, follow these steps:

1. Download and extract this project's ZIP file from GitHub.
2. Extract the file `VocabularyFoxReadyToRun.zip`.
3. Double-click the file `VocabularyFox.exe`.

**INFO**: The file `VocabularyFox.exe` contains the VocabularyFox Java classes and startup code for Windows. The startup code looks for a Java Runtime installed on your system. If it doesn’t find one, then it uses the OpenJDK Java Runtime in the extracted `jre` folder. If a Java Runtime is installed on your system, you can delete the `jre` folder.

The file `VocabularyFox.exe` was created with Launch4J.

Both Launch4J and OpenJDK are open-source software.

## Visual Tour

### Selecting a Quiz

Select a vocabulary quiz from the list. Use the input field to find a quiz if the list is long.

<img src="pics/pic01.png"/>

**INFO**: Quizzes are located in the `quizzes` folder. If no such folder exists, then VocabularyFox creates a `quizzes` folder with a sample quiz.

To select quiz options, click the gear icon <img src="pics/pic02.png"/>. This opens the preferences dialog. It displays the version number, lets you change the language of the user interface (currently English and German), and lets you modify quiz options:

<img src="pics/pic03.png"/>

### Taking a Quiz

This is a sample of a French quiz:

<img src="pics/pic06.png"/>

**TIP**: To make typing easier when entering an answer, you can use a set of abbreviations (depending on the language). The screenshots below show the supported abbreviations for English and French quizzes:

<img src="pics/pic04.png"/> <img src="pics/pic05.png"/>

_(French quizzes only)_ To enter French text with a non-French keyboard, click button (1). This opens a list of French characters. Select one of the characters to insert it into you answer.

<img src="pics/pic07.png"/>

_(French quizzes only)_ Buttons (2) appear with French nouns whose article doesn’t indicate the gender. After typing the answer, click either the "m." (masculine) of "f." (feminine) button to continue with the next question.

<img src="pics/pic08.png"/>

### Reviewing the Quiz Results

After you have entered all answers, your results are listed:

<img src="pics/pic09.png"/>

The results are color-coded as follows: 
* Blue: Noun of masculine gender
* Dark red: Noun of feminine gender
* Orange: Correct answer on your second try
* Red: Correct answer on your third try - or no correct answer at all

To save your results as a web page to the file system, click on the download page icon <img src="pics/pic10.png"/>.

If you want to repeat a quiz, you can repeat either the entire quiz or just the words with wrong answers:

<img src="pics/pic11.png"/>

## How to Add Your Own Quizzes

It is easy to add your own quizzes to VocabularyFox!
* See [instructions in English](doc/InstructionsEnglish.pdf).
* See [instructions in German](doc/InstructionsGerman.pdf).
