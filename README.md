# CleanHangman

Java Hangman game implemented using Clean Architecture

See UMLs [here](https://drive.google.com/file/d/1FNpa_FpKb1q6PPdpMBOgtjBByZgz4DaS/view?usp=sharing).

## Requirements

- User can create an account (max. 10 users)
  - Store username and number of words successfully guessed
- First user is root
  - Cannot be removed
  - Can remove others
- User must log into their account before playing
- Program can randomly pick a length (min. 7 letters, max. 21 letters)
- User can start a game
- During a game:
    - User can make guesses
    - User can get at most 7 guesses wrong
    - Program shows all instances of correctly guessed letters
    - Program shows '-' in place of each letter that has not been guessed
    - Program shows how many guesses a user has gotten wrong
    - Program shows list of all letters that has been guessed

## Entities

### `GuessWord`

- Stores a word to be guessed (e.g., “racecar”)
- Stores the view of a word as guessed by a user
  - e.g., "r--e--r" if both 'e' and 'r' were correctly guessed prior
- Can update view of the word

### `User`

- Stores username
- Stores number of words the user has successfully guessed
 
### `Game`

- Stores the current Word being guessed
- Stores the current User that is playing
- Stores a set of letters that has been guessed
- Stores how many guesses are wrong

## UseCases

### `AccountUseCase`
  - Creating an account
  - Logging into an account
  - Removing an account
### `ShowLeaderBoardUseCase`
  - Rank by alphabetical order
  - Rank by number of words guessed correctly
### `PlayGameUseCase`
  - Start game
  - Make guess

## Interface Adapters

### `TextFileRepository`

In Clean Architecture, interface adapters generally only translate an external
interface into something that our entities and use cases can understand. In
the `TextFileRepository` implementation, however, the repository performs some
rather involved logic. In particular, it is implementing the logic of storing
data in text files.
