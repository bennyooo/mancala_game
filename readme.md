<h2>Readme</h2>
To run this code it is necessary to install maven and load the dependencies in pom.xml    

To play the game you can start the server by using `mvn spring-boot:run` then you can run the `MancalaGameFrontEnd`  
The game will run with pre-defined players "player1" and "player2".  

"player1" can select holes from 1-6 where the left most hole is the first one.  
"player2" can also select holes from 1-6 where the right most hole is the first one, as they are playing upside down.  
  
The rules are slightly changed from the assignment's version: Players cannot only collect stones when they put the last stone in an empty 
regular hole on their side but also if they put the last stone in a regular empty hole on the opponents side. This creates a more dynamic gameplay.  

Also when the game ends, the remaining stones on each side are not counted towards the final player score.  
This also creates more dynamic as it forces players to remove their stones from their area.  
  
For the purpose of a faster game we only use 3 stones per hole instead of 6. However, this can be changed.