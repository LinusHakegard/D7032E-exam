This is a terminal game of Boomerang Australia

 A way to run the project is to download maven 3.9.5
 
 To compile:
 mvn clean compile
 
 To run (mac, linux):
 mvn exec:java-Dexec.mainClass="boomerang.Boomerang"-Dexec.args="2 Australia"
 
 To run (windows):
 mvn exec:java-D"exec.mainClass"="boomerang.Boomerang"-D"exec.args"="2
 Australia"
 
The first argument is the number of players, you can choose between 2-4. The second
 argument is the game version which needs to be “Australia”.
