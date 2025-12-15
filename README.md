######################## Project Overview: ########################

This is a small project i did for school making the game of Go in the terminal.
The point of the project was to gain some practice using Java as a langauge

The basic game loop works as follows:
A stone is placed, it first checks where that stone would be captured by the current game state
Then it checks all avaible spots around the newly placed stone to see if they might now be considered 'catpured' by a ring of stones created by
the new stone.
This basic loop continues until no places are left to add stones and then the game is over

######################## To run/project notes: ########################

Command to run: 
javac go.java && java go (or the two commands seperately)

Im not going to put the rules of Go, i assume you now how to play
  * Board key:
  * 0 = blank
  * 1 = white stone
  * 2 = black stone
  * 3 = blank space that belongs to white (through a capture)
  * 4 = blank space that belongs to black (through a capture)
    
NOTE: White goes first as usual



